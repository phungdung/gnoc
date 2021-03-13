package com.viettel.gnoc.sr.business;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.SrCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.sr.repository.SRCatalogRepository2;
import com.viettel.gnoc.sr.repository.SRConfigRepository;
import com.viettel.gnoc.sr.repository.SRHisRepository;
import com.viettel.gnoc.sr.repository.SrRepository;
import com.viettel.security.PassTranformer;
import com.viettel.vmsa.ResultCreateDtByFileInput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
@PrepareForTest({SrNocProBusinessImplTest.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, ObjectMapper.class, Workbook.class, ResultCreateDtByFileInput.class, ExcelWriterUtils.class, CommonExport.class,
    DateUtil.class, DataUtil.class, PassTranformer.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})
public class SrNocProBusinessImplTest {
  @InjectMocks
  SrNocProBusinessImpl srNocProBusiness;
  @Mock
  SrBusiness srBusiness;

  @Mock
  SrRepository srRepository;

  @Mock
  CatLocationRepository catLocationRepository;

  @Mock
  SRConfigRepository srConfigRepository;

  @Mock
  SRCatalogRepository2 srCatalogRepository2;

  @Mock
  UnitRepository unitRepository;

  @Mock
  SrCategoryServiceProxy srCategoryServiceProxy;

  @Mock
  SRHisRepository srHisRepository;

  @Mock
  SrOutsideBusiness srOutsideBusiness;

  @Test
  public void getListServiceArraySR_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    srNocProBusiness.getListServiceArraySR(null);

  }

  @Test
  public void getListServiceArraySR_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    srNocProBusiness.getListServiceArraySR("a");

  }

  @Test
  public void getListServiceArraySR_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListServiceArraySR("a");
  }

  @Test
  public void getListServiceArraySR_04() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListServiceArraySR("1");

  }

  @Test
  public void getListServiceArraySR_05() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    List<SRConfigDTO> srConfigDTOList = Mockito.spy(ArrayList.class);
    srConfigDTOList.add(srConfigDTO);

    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.SERVICE_ARRAY)).thenReturn(srConfigDTOList);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListServiceArraySR("1");

  }

  @Test
  public void getListServiceArraySR_06() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    List<SRConfigDTO> srConfigDTOList = Mockito.spy(ArrayList.class);
    srConfigDTOList.add(srConfigDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    List<SRCatalogDTO> srCatalogDTOList = Mockito.spy(ArrayList.class);
    srCatalogDTOList.add(srCatalogDTO);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(srCatalogDTOList);
    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.SERVICE_ARRAY)).thenReturn(srConfigDTOList);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListServiceArraySR("1");

  }

  @Test
  public void getListServiceArraySR_07() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    srConfigDTO.setServiceCode("1");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> srConfigDTOList = Mockito.spy(ArrayList.class);
    srConfigDTOList.add(srConfigDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceArray("1");
    srCatalogDTO.setCountry("1");
    srCatalogDTO.setServiceCode("1");
    List<SRCatalogDTO> srCatalogDTOList = Mockito.spy(ArrayList.class);
    srCatalogDTOList.add(srCatalogDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.DICH_VU_NOC)).thenReturn(srConfigDTOList);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(srCatalogDTOList);
    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.SERVICE_ARRAY)).thenReturn(srConfigDTOList);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListServiceArraySR("1");

  }


  @Test
  public void getListServiceGroupSR_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    srConfigDTO.setServiceCode("1");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> srConfigDTOList = Mockito.spy(ArrayList.class);
    srConfigDTOList.add(srConfigDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceArray("1");
    srCatalogDTO.setCountry("1");
    srCatalogDTO.setServiceCode("1");
    List<SRCatalogDTO> srCatalogDTOList = Mockito.spy(ArrayList.class);
    srCatalogDTOList.add(srCatalogDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.DICH_VU_NOC)).thenReturn(srConfigDTOList);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(srCatalogDTOList);
    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.SERVICE_ARRAY)).thenReturn(srConfigDTOList);
    srNocProBusiness.getListServiceGroupSR("1");
  }

  @Test
  public void getListServiceGroupSR_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    srConfigDTO.setServiceCode("1");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> srConfigDTOList = Mockito.spy(ArrayList.class);
    srConfigDTOList.add(srConfigDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceArray("1");
    srCatalogDTO.setCountry("1");
    srCatalogDTO.setServiceCode("1");
    List<SRCatalogDTO> srCatalogDTOList = Mockito.spy(ArrayList.class);
    srCatalogDTOList.add(srCatalogDTO);
    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.DICH_VU_NOC)).thenReturn(srConfigDTOList);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(srCatalogDTOList);
    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.SERVICE_ARRAY)).thenReturn(srConfigDTOList);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListServiceGroupSR("1");
  }

  @Test
  public void getListServiceGroupSR_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    srConfigDTO.setServiceCode("1");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> srConfigDTOList = Mockito.spy(ArrayList.class);
    srConfigDTOList.add(srConfigDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceArray("1");
    srCatalogDTO.setCountry("1");
    srCatalogDTO.setServiceCode("1");
    List<SRCatalogDTO> srCatalogDTOList = Mockito.spy(ArrayList.class);
    srCatalogDTOList.add(srCatalogDTO);

    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.SERVICE_GROUP)).thenReturn(srConfigDTOList);
    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.DICH_VU_NOC)).thenReturn(srConfigDTOList);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(srCatalogDTOList);
    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.SERVICE_ARRAY)).thenReturn(srConfigDTOList);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListServiceGroupSR("1");
  }

  @Test
  public void getListServiceGroupSR_04() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    srConfigDTO.setServiceCode("1");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> srConfigDTOList = Mockito.spy(ArrayList.class);
    srConfigDTOList.add(srConfigDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceArray("1");
    srCatalogDTO.setCountry("1");
    srCatalogDTO.setServiceCode("1");
    List<SRCatalogDTO> srCatalogDTOList = Mockito.spy(ArrayList.class);
//    srCatalogDTOList.add(srCatalogDTO);

    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.SERVICE_GROUP)).thenReturn(srConfigDTOList);
    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.DICH_VU_NOC)).thenReturn(srConfigDTOList);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(srCatalogDTOList);
    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.SERVICE_ARRAY)).thenReturn(srConfigDTOList);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListServiceGroupSR("1");
  }

  @Test
  public void getListServiceGroupSR_05() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    srConfigDTO.setServiceCode("1");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> srConfigDTOList = Mockito.spy(ArrayList.class);
    srConfigDTOList.add(srConfigDTO);

    List<SRConfigDTO> srConfigDTOList1 = Mockito.spy(ArrayList.class);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceArray("1");
    srCatalogDTO.setCountry("1");
    srCatalogDTO.setServiceCode("1");
    List<SRCatalogDTO> srCatalogDTOList = Mockito.spy(ArrayList.class);
    srCatalogDTOList.add(srCatalogDTO);

    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.SERVICE_GROUP)).thenReturn(srConfigDTOList);
    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.DICH_VU_NOC)).thenReturn(srConfigDTOList1);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(srCatalogDTOList);
    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.SERVICE_ARRAY)).thenReturn(srConfigDTOList);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListServiceGroupSR("1");
  }

  @Test
  public void getListServiceGroupSR_06() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    srConfigDTO.setServiceCode("1");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> srConfigDTOList = Mockito.spy(ArrayList.class);
    srConfigDTOList.add(srConfigDTO);

    List<SRConfigDTO> srConfigDTOList1 = Mockito.spy(ArrayList.class);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceArray("1");
    srCatalogDTO.setCountry("1");
    srCatalogDTO.setServiceCode("1");
    srCatalogDTO.setServiceGroup("1");
    List<SRCatalogDTO> srCatalogDTOList = Mockito.spy(ArrayList.class);
    srCatalogDTOList.add(srCatalogDTO);

    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.SERVICE_GROUP)).thenReturn(srConfigDTOList);
    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.DICH_VU_NOC)).thenReturn(srConfigDTOList);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(srCatalogDTOList);
    PowerMockito.when(srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.SERVICE_ARRAY)).thenReturn(srConfigDTOList);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListServiceGroupSR("1");
  }

  @Test
  public void getListServiceSR_01(){
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
//    srNocProBusiness.getListServiceSR("1", "1");
    srNocProBusiness.getListServiceSR("1");
  }

  @Test
  public void getListServiceSR_02(){
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
//    srNocProBusiness.getListServiceSR("1", "0");
    srNocProBusiness.getListServiceSR("1");
  }

  @Test
  public void getListServiceSR_03(){
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
//    srNocProBusiness.getListServiceSR("1", "0");
    srNocProBusiness.getListServiceSR("1");
  }

  @Test
  public void getListServiceSR_04(){
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
//    srNocProBusiness.getListServiceSR("1", "1");
    srNocProBusiness.getListServiceSR("1");
  }

  @Test
  public void getListServiceSR_05(){
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    List<SRCatalogDTO> srCatalogDTOS = Mockito.spy(ArrayList.class);
    srCatalogDTOS.add(srCatalogDTO);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(srCatalogDTOS);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
//    srNocProBusiness.getListServiceSR("1", "1");
    srNocProBusiness.getListServiceSR("1");
  }

  @Test
  public void getListServiceSR_06(){
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceArray("1");
    srCatalogDTO.setCountry("1");
    srCatalogDTO.setServiceCode("1");
    srCatalogDTO.setServiceGroup("1");
    List<SRCatalogDTO> srCatalogDTOS = Mockito.spy(ArrayList.class);

    srCatalogDTOS.add(srCatalogDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setAutomation("1");
    srConfigDTO.setServiceCode("1");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    lstConfig.add(srConfigDTO);

    PowerMockito.when(srConfigRepository.getByConfigGroup(any())).thenReturn(lstConfig);
    PowerMockito.when(srCatalogRepository2.getListSRCatalogDTO(any())).thenReturn(srCatalogDTOS);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
//    srNocProBusiness.getListServiceSR("1", "1");
    srNocProBusiness.getListServiceSR("1");
  }


  @Test
  public void getListUnitServiceSR_01() {
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListUnitServiceSR("1");
  }

  @Test
  public void getListUnitServiceSR_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListUnitServiceSR("1");
  }

  @Test
  public void getListUnitServiceSR_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    List<SRCatalogDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(srCatalogDTO);

    PowerMockito.when(srCatalogRepository2.getListCatalogWithRoleAndUnit(anyString())).thenReturn(lst);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListUnitServiceSR("1");
  }

  @Test
  public void getListUnitServiceSR_04() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    List<SRCatalogDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(srCatalogDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    lstConfig.add(srConfigDTO);

    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstConfig);
    PowerMockito.when(srCatalogRepository2.getListCatalogWithRoleAndUnit(anyString())).thenReturn(lst);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListUnitServiceSR("1");
  }

  @Test
  public void getListUnitServiceSR_05() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    List<SRCatalogDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(srCatalogDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
//    srConfigDTO.setServiceCode("1");
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    lstConfig.add(srConfigDTO);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    List<UnitDTO> lstUnit = Mockito.spy(ArrayList.class);
    lstUnit.add(unitDTO);

    PowerMockito.when(unitRepository.getListUnit(any())).thenReturn(lstUnit);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstConfig);
    PowerMockito.when(srCatalogRepository2.getListCatalogWithRoleAndUnit(anyString())).thenReturn(lst);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListUnitServiceSR("1");
  }

  @Test
  public void getListUnitServiceSR_06() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    srCatalogDTO.setServiceCode("1");
    srCatalogDTO.setStatus("A");
    srCatalogDTO.setExecutionUnit("1,1,1,1");
    List<SRCatalogDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(srCatalogDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setServiceCode("1");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    lstConfig.add(srConfigDTO);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    unitDTO.setUnitCode("1");
    unitDTO.setUnitName("1");
    List<UnitDTO> lstUnit = Mockito.spy(ArrayList.class);
    lstUnit.add(unitDTO);

    PowerMockito.when(unitRepository.getListUnit(any())).thenReturn(lstUnit);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstConfig);
    PowerMockito.when(srCatalogRepository2.getListCatalogWithRoleAndUnit(anyString())).thenReturn(lst);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListUnitServiceSR("1");
  }

  @Test
  public void getListRoleServiceSR_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListRoleServiceSR("1");
  }

  @Test
  public void getListRoleServiceSR_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListRoleServiceSR("1");
  }

  @Test
  public void getListRoleServiceSR_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setCountry("1");
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    lstConfig.add(srConfigDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    List<SRCatalogDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(srCatalogDTO);

    PowerMockito.when(srCatalogRepository2.getListCatalogWithRoleAndUnit(anyString())).thenReturn(lst);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListRoleServiceSR("1");
  }

  @Test
  public void getListRoleServiceSR_04() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setCountry("1");
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    lstConfig.add(srConfigDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    List<SRCatalogDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(srCatalogDTO);


    PowerMockito.when(srCatalogRepository2.getListCatalogWithRoleAndUnit(anyString())).thenReturn(lst);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstConfig);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListRoleServiceSR("1");
  }

  @Test
  public void getListRoleServiceSR_05() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setCountry("1");
    srConfigDTO.setConfigCode("1");
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    lstConfig.add(srConfigDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceCode("1");
    srCatalogDTO.setStatus("A");
    srCatalogDTO.setExecutionUnit("1,1,1,1");
    srCatalogDTO.setRoleCode("1");
    List<SRCatalogDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(srCatalogDTO);

    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    List<SRRoleDTO> lstRole = Mockito.spy(ArrayList.class);
    lstRole.add(srRoleDTO);

    PowerMockito.when(srCategoryServiceProxy.getListSRRoleDTO(any())).thenReturn(lstRole);
    PowerMockito.when(srCatalogRepository2.getListCatalogWithRoleAndUnit(anyString())).thenReturn(lst);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstConfig);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListRoleServiceSR("1");
  }

  @Test
  public void getListRoleServiceSR_06() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    CatLocationDTO catLocationDTO = Mockito.spy(CatLocationDTO.class);
    catLocationDTO.setLocationId("1");
    List<CatLocationDTO> lstLocation = Mockito.spy(ArrayList.class);
    lstLocation.add(catLocationDTO);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setCountry("1");
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setServiceCode("1");
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    lstConfig.add(srConfigDTO);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceCode("1");
    srCatalogDTO.setStatus("A");
    srCatalogDTO.setExecutionUnit("1,1,1,1");
    srCatalogDTO.setRoleCode("1");
    srCatalogDTO.setCountry("1");
    List<SRCatalogDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(srCatalogDTO);

    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    srRoleDTO.setRoleCode("1");
    srRoleDTO.setRoleName("1");
    List<SRRoleDTO> lstRole = Mockito.spy(ArrayList.class);
    lstRole.add(srRoleDTO);

    PowerMockito.when(srCategoryServiceProxy.getListSRRoleDTO(any())).thenReturn(lstRole);
    PowerMockito.when(srCatalogRepository2.getListCatalogWithRoleAndUnit(anyString())).thenReturn(lst);
    PowerMockito.when(srConfigRepository.getByConfigGroup(anyString())).thenReturn(lstConfig);
    PowerMockito.when(catLocationRepository.getCatLocationByLevel(anyString())).thenReturn(lstLocation);
    srNocProBusiness.getListRoleServiceSR("1");
  }

  @Test
  public void getListStatusSR_01() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<String> lstSrCode = Mockito.spy(ArrayList.class);
    lstSrCode.add("1");
    List<String> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add("1");
    srNocProBusiness.getListStatusSR(lstSrCode,lstStatus, null, "22/05/2021 03:03:03");
  }

  @Test
  public void getListStatusSR_02() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<String> lstSrCode = Mockito.spy(ArrayList.class);
    lstSrCode.add("1");
    List<String> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add("1");
    srNocProBusiness.getListStatusSR(lstSrCode,lstStatus, "22/05/2020 03:03:03", null);
  }

  @Test
  public void getListStatusSR_03() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<String> lstSrCode = Mockito.spy(ArrayList.class);
    lstSrCode.add("1");
    List<String> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add("1");
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    List<SrInsiteDTO> lstSR = Mockito.spy(ArrayList.class);
//    lstSR.add(srInsiteDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstSR);
    PowerMockito.when(srRepository.getListSR(any())).thenReturn(datatable);
    srNocProBusiness.getListStatusSR(lstSrCode,lstStatus, "22/05/2020 03:03:03", "22/05/2021 03:03:03");
  }

  @Test
  public void getListStatusSR_04() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<String> lstSrCode = Mockito.spy(ArrayList.class);
    lstSrCode.add("1");
    List<String> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add("1");
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setUpdatedTime(new Date());
    List<SrInsiteDTO> lstSR = Mockito.spy(ArrayList.class);
    lstSR.add(srInsiteDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstSR);
    PowerMockito.when(srRepository.getListSR(any())).thenReturn(datatable);
    srNocProBusiness.getListStatusSR(lstSrCode,lstStatus, "22/05/2022 03:03:03", "22/05/2021 03:03:03");
  }

  @Test
  public void getListStatusSR_05() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<String> lstSrCode = Mockito.spy(ArrayList.class);
    lstSrCode.add("1");
    List<String> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add("1");
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setUpdatedTime(new Date());
    List<SrInsiteDTO> lstSR = Mockito.spy(ArrayList.class);
    lstSR.add(srInsiteDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstSR);
    PowerMockito.when(srRepository.getListSR(any())).thenReturn(datatable);
    srNocProBusiness.getListStatusSR(lstSrCode,lstStatus, "22/05/2020 03:03:03", "22/05/2015 03:03:03");
  }

  @Test
  public void getListStatusSR_06() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<String> lstSrCode = Mockito.spy(ArrayList.class);
    lstSrCode.add("1");
    List<String> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add("1");
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setUpdatedTime(new Date());
    List<SrInsiteDTO> lstSR = Mockito.spy(ArrayList.class);
    lstSR.add(srInsiteDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstSR);
    PowerMockito.when(srRepository.getListSR(any())).thenReturn(datatable);
    srNocProBusiness.getListStatusSR(lstSrCode,lstStatus, "22/05/2020 03:03:03", "1");
  }

  @Test
  public void getListStatusSR_07() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<String> lstSrCode = Mockito.spy(ArrayList.class);
    lstSrCode.add("1");
    List<String> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add("1");
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setUpdatedTime(new Date());
    List<SrInsiteDTO> lstSR = Mockito.spy(ArrayList.class);
    lstSR.add(srInsiteDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstSR);
    PowerMockito.when(srRepository.getListSR(any())).thenReturn(datatable);
    srNocProBusiness.getListStatusSR(lstSrCode,lstStatus, "1", "1");
  }

  @Test
  public void getListStatusSR_08() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<String> lstSrCode = Mockito.spy(ArrayList.class);
    lstSrCode.add("1");
    List<String> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add("1");
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setUpdatedTime(new Date());
    List<SrInsiteDTO> lstSR = Mockito.spy(ArrayList.class);
    lstSR.add(srInsiteDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstSR);
    PowerMockito.when(srRepository.getListSR(any())).thenReturn(datatable);
    srNocProBusiness.getListStatusSR(lstSrCode,lstStatus, "22/05/2020 03:03:03", "22/05/2021 03:03:03");
  }

  @Test
  public void getListStatusSR_09() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    List<String> lstSrCode = Mockito.spy(ArrayList.class);
    lstSrCode.add("1");
    List<String> lstStatus = Mockito.spy(ArrayList.class);
    lstStatus.add("1");
    SrInsiteDTO srInsiteDTO = Mockito.spy(SrInsiteDTO.class);
    srInsiteDTO.setUpdatedTime(new Date());
    srInsiteDTO.setStatus("1");
    srInsiteDTO.setSrCode("1");
    srInsiteDTO.setSrId(1L);
    List<SrInsiteDTO> lstSR = Mockito.spy(ArrayList.class);
    lstSR.add(srInsiteDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstSR);
    PowerMockito.when(srRepository.getListSR(any())).thenReturn(datatable);
    ResultDTO resultDTO = srNocProBusiness.getListStatusSR(lstSrCode,lstStatus, "22/05/2020 03:03:03", "22/05/2021 03:03:03");
    Assert.assertEquals(resultDTO.getKey(), "SUCCESS");
  }

  @Test
  public void createSRForNoc_01() {
    SRDTO srDTO = Mockito.spy(SRDTO.class);
    ResultDTO res = Mockito.spy(ResultDTO.class);
    res.setKey("SUCCESS");
    PowerMockito.when(srOutsideBusiness.createSRByConfigGroup(any(), anyString())).thenReturn(res);
    ResultDTO resultDTO = srNocProBusiness.createSRForNoc(srDTO);
    Assert.assertEquals(resultDTO.getKey(), "SUCCESS");
  }

}
