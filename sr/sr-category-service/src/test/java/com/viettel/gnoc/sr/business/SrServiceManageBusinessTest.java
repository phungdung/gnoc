package com.viettel.gnoc.sr.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.repository.SRCatalogRepository;
import com.viettel.gnoc.sr.repository.SRServiceManageRepository;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
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
import org.slf4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SRServiceManageBusinessImpl.class, FileUtils.class, CommonImport.class,
    TicketProvider.class, I18n.class, CommonExport.class})
@PowerMockIgnore({"javax.management.", "com.sun.org.apache.xerces.",
    "javax.xml.*", "org.xml.*", "org.w3c.dom.*",
    "com.sun.org.apache.xalan.", "javax.activation.*", "jdk.xml.internal.*", "com.sun.org.*"})

public class SrServiceManageBusinessTest {

  @Mock
  protected SRServiceManageRepository srServiceManageRepository;

  @Mock
  protected SRCatalogRepository srCatalogRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  protected CatLocationBusiness catLocationBusiness;

  @Mock
  protected SRCatalogBusiness srCatalogBusiness;

  @Mock
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @InjectMocks
  protected SRServiceManageBusinessImpl srServiceManageBusiness;

  static void setFinalStatic(Field fields, Object val) throws Exception {
    fields.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(fields, fields.getModifiers() & ~Modifier.FINAL);
    fields.set(null, val);
  }

  @Test
  public void test_getListSearchSRServiceArray_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setStatus("1");
    List<SRConfigDTO> configDTOList = Mockito.spy(ArrayList.class);
    configDTOList.add(srConfigDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(configDTOList);
    SRCatalogDTO catalogDTO = Mockito.spy(SRCatalogDTO.class);
    catalogDTO.setCountry("281");
    List<SRCatalogDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(catalogDTO);

    PowerMockito.when(srServiceManageRepository.checkEnableGroupNotConvert(any()))
        .thenReturn(configDTOList);
    PowerMockito.when(srCatalogRepository.getListSRCatalogDTO(any())).thenReturn(lst);
    PowerMockito.when(srServiceManageRepository.getListSearchSRServiceArray(any()))
        .thenReturn(datatable);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    Datatable datatable1 = srServiceManageBusiness.getListSearchSRServiceArray(srConfigDTO);
    assertEquals(datatable.getPages(), datatable1.getPages());

  }

  @Test
  public void test_insertOrUpdateServiceArray_01() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("asdsdsdsdsd");
    srConfigDTO.setConfigName("asdsdsdsdsd");
    srConfigDTO.setCountry("281");
    srConfigDTO.setCreatedUser(userToken.getUserName());
    srConfigDTO.setCreatedTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
    srConfigDTO.setUpdatedTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
    srConfigDTO.setUpdatedUser(userToken.getUserName());
    srConfigDTO.setConfigGroup(Constants.SR_CATALOG.SERVICE_ARRAY);
    srConfigDTO.setStatus(Constants.SR_CATALOG.SERVICE_STATUS);

    // setFinalStatic(SRServiceManageBusinessImpl.class.getDeclaredField("log"), logger);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(srServiceManageRepository.insertOrUpdateService(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = srServiceManageBusiness.insertOrUpdateServiceArray(srConfigDTO);
    Assert.assertEquals(result, resultInSideDto);
  }

  @Test
  public void test_insertOrUpdateServiceArray() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByName(any()))
        .thenReturn(false);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByCode(any())).thenReturn(true);
    ResultInSideDto result = srServiceManageBusiness.insertOrUpdateServiceArray(srConfigDTO);
    assertEquals(result.getKey(), "DUPLICATE");
  }

  @Test
  public void test_insertOrUpdateServiceArray_1() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    srConfigDTO.setStatus("I");
    SRConfigDTO dtoUpdate = Mockito.spy(SRConfigDTO.class);
    dtoUpdate.setConfigCode("1");
    dtoUpdate.setConfigName("1");
    dtoUpdate.setStatus("I");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    PowerMockito.when(srServiceManageRepository.getSRServiceArrayByDTO(any()))
        .thenReturn(dtoUpdate);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExisted(any())).thenReturn(true);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByName(any())).thenReturn(true);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByCode(any())).thenReturn(true);
    PowerMockito.when(srServiceManageRepository.insertOrUpdateService(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = srServiceManageBusiness.insertOrUpdateServiceArray(srConfigDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void insertOrUpdateServiceArray_2() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    srConfigDTO.setStatus("I");
    SRConfigDTO dtoUpdate = Mockito.spy(SRConfigDTO.class);
    dtoUpdate.setConfigCode("1");
    dtoUpdate.setConfigName("1");
    dtoUpdate.setStatus("A");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    PowerMockito.when(srServiceManageRepository.getSRServiceArrayByDTO(any()))
        .thenReturn(dtoUpdate);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExisted(any())).thenReturn(true);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByName(any())).thenReturn(true);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByCode(any())).thenReturn(true);
//    PowerMockito.when(srServiceManageRepository.insertOrUpdateService(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = srServiceManageBusiness.insertOrUpdateServiceArray(srConfigDTO);
    assertEquals(result.getKey(), "DUPLICATE");
  }

  @Test
  public void insertOrUpdateServiceArray_3() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    srConfigDTO.setStatus("I");
    SRConfigDTO dtoUpdate = Mockito.spy(SRConfigDTO.class);
    dtoUpdate.setConfigCode("2");
    dtoUpdate.setConfigName("2");
    dtoUpdate.setStatus("A");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    PowerMockito.when(srServiceManageRepository.getSRServiceArrayByDTO(any()))
        .thenReturn(dtoUpdate);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExisted(any())).thenReturn(true);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByName(any())).thenReturn(true);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByCode(any())).thenReturn(true);
//    PowerMockito.when(srServiceManageRepository.insertOrUpdateService(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = srServiceManageBusiness.insertOrUpdateServiceArray(srConfigDTO);
    assertEquals(result.getKey(), "DUPLICATE");
  }

  @Test
  public void insertOrUpdateServiceArray_4() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    srConfigDTO.setStatus("I");
    SRConfigDTO dtoUpdate = Mockito.spy(SRConfigDTO.class);
    dtoUpdate.setConfigCode("2");
    dtoUpdate.setConfigName("2");
    dtoUpdate.setStatus("A");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    PowerMockito.when(srServiceManageRepository.getSRServiceArrayByDTO(any()))
        .thenReturn(dtoUpdate);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExisted(any())).thenReturn(false);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByName(any())).thenReturn(true);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByCode(any())).thenReturn(true);
    PowerMockito.when(srServiceManageRepository.insertOrUpdateService(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = srServiceManageBusiness.insertOrUpdateServiceArray(srConfigDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void insertOrUpdateServiceArray_5() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("aaaa");
    srConfigDTO.setConfigName("2");
    srConfigDTO.setStatus("I");
    srConfigDTO.setConfigId(1L);
    SRConfigDTO dtoUpdate = Mockito.spy(SRConfigDTO.class);
    dtoUpdate.setConfigCode("2");
    dtoUpdate.setConfigName("1");
    dtoUpdate.setStatus("A");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByName(any())).thenReturn(true);
    PowerMockito.when(srServiceManageRepository.getSRServiceArrayDetail(any()))
        .thenReturn(dtoUpdate);
    PowerMockito.when(srServiceManageRepository.getSRServiceArrayByDTO(any()))
        .thenReturn(dtoUpdate);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExisted(any())).thenReturn(false);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByName(any())).thenReturn(true);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByCode(any())).thenReturn(true);
    PowerMockito.when(srServiceManageRepository.insertOrUpdateService(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = srServiceManageBusiness.insertOrUpdateServiceArray(srConfigDTO);
    assertEquals(result.getKey(), "DUPLICATE");
  }

  @Test
  public void insertOrUpdateServiceArray_6() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("aaaa");
    srConfigDTO.setConfigName("2");
    srConfigDTO.setStatus("I");
    srConfigDTO.setConfigId(1L);
    SRConfigDTO dtoUpdate = Mockito.spy(SRConfigDTO.class);
    dtoUpdate.setConfigCode("2");
    dtoUpdate.setConfigName("1");
    dtoUpdate.setStatus("Active");
    List<SRConfigDTO> list = Mockito.spy(ArrayList.class);
    list.add(dtoUpdate);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    PowerMockito.when(srServiceManageRepository.getSrConfigExistedByName(any())).thenReturn(list);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByName(any()))
        .thenReturn(false);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByName(any()))
        .thenReturn(false);
    PowerMockito.when(srServiceManageRepository.getSRServiceArrayDetail(any()))
        .thenReturn(dtoUpdate);
    PowerMockito.when(srServiceManageRepository.getSRServiceArrayByDTO(any()))
        .thenReturn(dtoUpdate);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExisted(any())).thenReturn(false);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByCode(any())).thenReturn(true);
    PowerMockito.when(srServiceManageRepository.insertOrUpdateService(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = srServiceManageBusiness.insertOrUpdateServiceArray(srConfigDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }


  @Test
  public void test_exportDataServiceArray() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list"))
        .thenReturn("2");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.countryDisplay"))
        .thenReturn("3");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation"))
        .thenReturn("4");
    PowerMockito.mockStatic(CommonExport.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setStatus("A");
    srConfigDTO.setCountry("1");
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<SRConfigDTO> data = Mockito.spy(ArrayList.class);
    data.add(srConfigDTO);
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    PowerMockito.when(srServiceManageRepository.getByConfigGroup(any())).thenReturn(data);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    PowerMockito.when(srServiceManageRepository.getListDataExportServiceArray(any()))
        .thenReturn(data);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    File file = srServiceManageBusiness.exportDataServiceArray(srConfigDTO);
    assertNotNull(file);
  }


  @Test
  public void test_exportDataServiceGroup() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceGroup.title.export.list"))
        .thenReturn("2");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.countryDisplay"))
        .thenReturn("3");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation"))
        .thenReturn("4");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list"))
        .thenReturn("5");
    PowerMockito.mockStatic(CommonExport.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setStatus("A");
    srConfigDTO.setCountry("1");
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<SRConfigDTO> data = Mockito.spy(ArrayList.class);
    data.add(srConfigDTO);
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");

    PowerMockito.when(srServiceManageRepository.getByConfigGroup(any())).thenReturn(data);
    PowerMockito.when(commonStreamServiceProxy.insertLog(any())).thenReturn(resultInSideDto);
    PowerMockito.when(srServiceManageRepository.getListDataExportServiceGroup(any()))
        .thenReturn(data);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    File file = srServiceManageBusiness.exportDataServiceGroup(srConfigDTO);
    assertNotNull(file);
  }

  @Test
  public void test_getTemplateServiceArray() throws Exception {

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("2");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.countryDisplay")).thenReturn("3");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("4");
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setStatus("A");
    srConfigDTO.setCountry("2");
    srConfigDTO.setConfigCode("1");
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(srConfigDTO);
    srServiceManageBusiness.setMapAutomation();
    PowerMockito.when(srServiceManageRepository.getByConfigGroup(anyString())).thenReturn(lstData);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    File file = srServiceManageBusiness.getTemplateServiceArray();
    assertNotNull(file);
  }

  @Test
  public void test_getTemplateServiceGroup() throws Exception {

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceGroup.title.export.list")).thenReturn("5");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("2");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.countryDisplay")).thenReturn("3");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("4");
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setStatus("A");
    srConfigDTO.setCountry("2");
    srConfigDTO.setConfigCode("1");
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstData = Mockito.spy(ArrayList.class);
    lstData.add(srConfigDTO);
    srServiceManageBusiness.setMapAutomation();
    PowerMockito.when(srServiceManageRepository.getByConfigGroup(anyString())).thenReturn(lstData);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    File file = srServiceManageBusiness.getTemplateServiceGroup();
    assertNotNull(file);
  }

  @Test
  public void test_importDataServiceArray() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
//    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain", "some xml".getBytes());
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(null);
    assertEquals(result.getKey(), "FILE_IS_NULL");
  }

  @Test
  public void test_importDataServiceArray_1() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_2() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"deviceIdStr", "marketName", "regionSoft", "arrayName",
        "networkType", "deviceType", "nodeIp", "nodeCode", "deviceName", "createUserName",
        "comments", "mrSoftDisplay", "mrConfirmSoftDisplay", "impactNode", "numberOfCrStr",
        "boUnitSoftId", "cdIdName", "groupCode", "vendor", "stationCode", "statusName",
        "dateIntegratedStr", "mrTypeStr", "resultImport"};
    lstHeader.add(header);

    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_3() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"0", "1", "2", "3", "4", "5"};
    lstHeader.add(header);

    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_4() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1(*)", "2", "3", "4", "5"};
    lstHeader.add(header);

    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_5() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1", "3", "4", "5"};
    lstHeader.add(header);

    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_6() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "3", "4", "5"};
    lstHeader.add(header);

    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_7() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "4", "5"};
    lstHeader.add(header);

    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_8() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "5"};
    lstHeader.add(header);

    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_9() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    lstHeader.add(header);

    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 1000))
        .thenReturn(null);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "NODATA");
  }

  @Test
  public void test_importDataServiceArray_10() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    lstHeader.add(header);
    List<Object[]> lstHeader1 = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 2000; i++) {
      lstHeader1.add(header);
    }
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000))
        .thenReturn(lstHeader1);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "DATA_OVER");
  }

//  @Test
//  public void test_importDataServiceArray_11() throws Exception {
//    Logger logger = PowerMockito.mock(Logger.class);
//    PowerMockito.doNothing().when(logger).debug(any());
//    PowerMockito.mockStatic(TicketProvider.class);
//    UserToken userToken = Mockito.spy(UserToken.class);
//    PowerMockito.mockStatic(I18n.class);
//    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
//    PowerMockito.mockStatic(FileUtils.class);
//    PowerMockito.mockStatic(CommonImport.class);
//    userToken.setDeptId(1L);
//    userToken.setUserID(999999L);
//    userToken.setUserName("thanhlv12");
//    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
//    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
//    String filePath = "/temp";
//    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
//    String[] header = new String[]{"1","1 (*)","1 (*)","1 (*)","1","1 (*)"};
//    String[] header1 = new String[]{"1","1 (*)","1 (*)","1E","1","1 (*)"};
//    lstHeader.add(header);
//    List<Object[]> lstHeader1 = Mockito.spy(ArrayList.class);
//    lstHeader1.add(header);
//    lstHeader1.add(header1);
//    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
//    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain", "some xml".getBytes());
//    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
//    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
//    itemDataCRInside.setValueStr(1L);
//    itemDataCRInside.setDisplayStr("1");
//    lstCountryName.add(itemDataCRInside);
//    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
//    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
//    srConfigDTO.setConfigCode("1");
//    lstConfig.add(srConfigDTO);
//
//
//    PowerMockito.when(srServiceManageRepository.getByConfigGroup(anyString())).thenReturn(lstConfig);
//    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountryName);
//    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2)).thenReturn(lstHeader);
//    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000)).thenReturn(lstHeader1);
//    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
//    assertEquals(result.getKey(), "SUCCESS");
//  }

  @Test
  public void test_importDataServiceArray_12() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("b");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("c");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    String[] header1 = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    for (int i = 0; i < 300; i++) {
      header1[1] += "1";
    }
    lstHeader.add(header);
    List<Object[]> lstHeader1 = Mockito.spy(ArrayList.class);
    lstHeader1.add(header);
    lstHeader1.add(header1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    lstConfig.add(srConfigDTO);

    PowerMockito.when(srServiceManageRepository.getByConfigGroup(anyString()))
        .thenReturn(lstConfig);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000))
        .thenReturn(lstHeader1);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_13() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("a");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.countryDisplay")).thenReturn("b");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("c");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    String[] header1 = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
//    for(int i=0;i<300;i++){
//      header1[1] += "1";
//    }
    header1[1] = "";
    lstHeader.add(header);
    List<Object[]> lstHeader1 = Mockito.spy(ArrayList.class);
    lstHeader1.add(header);
    lstHeader1.add(header1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    lstConfig.add(srConfigDTO);

    PowerMockito.when(srServiceManageRepository.getByConfigGroup(anyString()))
        .thenReturn(lstConfig);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000))
        .thenReturn(lstHeader1);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_14() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("a");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.countryDisplay")).thenReturn("b");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("c");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    String[] header1 = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    header1[2] = "";
    lstHeader.add(header);
    List<Object[]> lstHeader1 = Mockito.spy(ArrayList.class);
    lstHeader1.add(header);
    lstHeader1.add(header1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    lstConfig.add(srConfigDTO);

    PowerMockito.when(srServiceManageRepository.getByConfigGroup(anyString()))
        .thenReturn(lstConfig);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000))
        .thenReturn(lstHeader1);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_15() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("a");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.countryDisplay")).thenReturn("b");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("c");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    String[] header1 = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    for (int i = 0; i < 300; i++) {
      header1[2] += "1";
    }
    lstHeader.add(header);
    List<Object[]> lstHeader1 = Mockito.spy(ArrayList.class);
    lstHeader1.add(header);
    lstHeader1.add(header1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    lstConfig.add(srConfigDTO);

    PowerMockito.when(srServiceManageRepository.getByConfigGroup(anyString()))
        .thenReturn(lstConfig);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000))
        .thenReturn(lstHeader1);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_16() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("b");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("c");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    String[] header1 = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    header1[3] = "";
    lstHeader.add(header);
    List<Object[]> lstHeader1 = Mockito.spy(ArrayList.class);
    lstHeader1.add(header);
    lstHeader1.add(header1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    lstConfig.add(srConfigDTO);

    PowerMockito.when(srServiceManageRepository.getByConfigGroup(anyString()))
        .thenReturn(lstConfig);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000))
        .thenReturn(lstHeader1);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_17() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("b");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("c");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    String[] header1 = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    for (int i = 0; i < 300; i++) {
      header1[3] += "1";
    }
    lstHeader.add(header);
    List<Object[]> lstHeader1 = Mockito.spy(ArrayList.class);
    lstHeader1.add(header);
    lstHeader1.add(header1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    lstConfig.add(srConfigDTO);

    PowerMockito.when(srServiceManageRepository.getByConfigGroup(anyString()))
        .thenReturn(lstConfig);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000))
        .thenReturn(lstHeader1);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_18() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("a");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.countryDisplay")).thenReturn("b");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("c");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    String[] header1 = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    header1[4] = "";
//    for(int i=0; i<300; i++){
//      header1[4] += "1";
//    }
    lstHeader.add(header);
    List<Object[]> lstHeader1 = Mockito.spy(ArrayList.class);
    lstHeader1.add(header);
    lstHeader1.add(header1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    lstConfig.add(srConfigDTO);

    PowerMockito.when(srServiceManageRepository.getByConfigGroup(anyString()))
        .thenReturn(lstConfig);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000))
        .thenReturn(lstHeader1);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_19() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("a");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.countryDisplay")).thenReturn("b");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("c");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    String[] header1 = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    header1[3] = "1";
    header1[4] = "1";
//    for(int i=0; i<300; i++){
//      header1[4] += "1";
//    }
    lstHeader.add(header);
    List<Object[]> lstHeader1 = Mockito.spy(ArrayList.class);
    lstHeader1.add(header);
    lstHeader1.add(header1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    lstConfig.add(srConfigDTO);

    PowerMockito.when(srServiceManageRepository.getByConfigGroup(anyString()))
        .thenReturn(lstConfig);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000))
        .thenReturn(lstHeader1);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_20() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("2");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.countryDisplay")).thenReturn("3");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("3");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    String[] header1 = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    header1[3] = "1";
    header1[4] = "1";
    header1[5] = "C";

    lstHeader.add(header);
    List<Object[]> lstHeader1 = Mockito.spy(ArrayList.class);
    lstHeader1.add(header);
    lstHeader1.add(header1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    lstConfig.add(srConfigDTO);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    List<SRCatalogDTO> lstCatalog = Mockito.spy(ArrayList.class);
    lstCatalog.add(srCatalogDTO);
    PowerMockito.when(srCatalogBusiness.getListSRCatalogDTO(any())).thenReturn(lstCatalog);
    PowerMockito.when(srServiceManageRepository.getByConfigGroup(anyString()))
        .thenReturn(lstConfig);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000))
        .thenReturn(lstHeader1);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_21() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("2");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.countryDisplay")).thenReturn("3");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("3");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    String[] header1 = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    header1[3] = "1";
    header1[4] = "1";
    header1[5] = "D";

    lstHeader.add(header);
    List<Object[]> lstHeader1 = Mockito.spy(ArrayList.class);
    lstHeader1.add(header);
    lstHeader1.add(header1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    lstConfig.add(srConfigDTO);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    List<SRCatalogDTO> lstCatalog = Mockito.spy(ArrayList.class);
    lstCatalog.add(srCatalogDTO);
    PowerMockito.when(srCatalogBusiness.getListSRCatalogDTO(any())).thenReturn(lstCatalog);
    PowerMockito.when(srServiceManageRepository.getByConfigGroup(anyString()))
        .thenReturn(lstConfig);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000))
        .thenReturn(lstHeader1);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_22() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("2");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.countryDisplay")).thenReturn("3");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("3");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    String[] header1 = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    header1[3] = "1";
    header1[4] = "1";
    header1[5] = "C";

    lstHeader.add(header);
    List<Object[]> lstHeader1 = Mockito.spy(ArrayList.class);
    lstHeader1.add(header);
    lstHeader1.add(header1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    lstConfig.add(srConfigDTO);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    List<SRCatalogDTO> lstCatalog = Mockito.spy(ArrayList.class);
    lstCatalog.add(srCatalogDTO);

    PowerMockito.when(srServiceManageRepository.getListSRConfigImport(any())).thenReturn(null);
    PowerMockito.when(srCatalogBusiness.getListSRCatalogDTO(any())).thenReturn(null);
    PowerMockito.when(srServiceManageRepository.getByConfigGroup(anyString()))
        .thenReturn(lstConfig);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000))
        .thenReturn(lstHeader1);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_23() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("a");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.countryDisplay")).thenReturn("b");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("c");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    String[] header1 = new String[]{"1", "1", "1", "1 (*)", "1", "1 (*)"};
    header1[3] = "1";
    header1[4] = "1";
    header1[5] = "D";

    lstHeader.add(header);
    List<Object[]> lstHeader1 = Mockito.spy(ArrayList.class);
    lstHeader1.add(header);
    lstHeader1.add(header1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    srConfigDTO.setStatus("A");
    lstConfig.add(srConfigDTO);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    List<SRCatalogDTO> lstCatalog = Mockito.spy(ArrayList.class);
    lstCatalog.add(srCatalogDTO);

    PowerMockito.when(srServiceManageRepository.getListSRConfigImport(any())).thenReturn(lstConfig);
    PowerMockito.when(srCatalogBusiness.getListSRCatalogDTO(any())).thenReturn(null);
    PowerMockito.when(srServiceManageRepository.getByConfigGroup(anyString()))
        .thenReturn(lstConfig);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000))
        .thenReturn(lstHeader1);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_24() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("2");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.countryDisplay")).thenReturn("3");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("3");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    String[] header1 = new String[]{"1", "1", "1", "1 (*)", "1", "1 (*)"};
    header1[3] = "1";
    header1[4] = "1";
    header1[5] = "C";

    lstHeader.add(header);
    List<Object[]> lstHeader1 = Mockito.spy(ArrayList.class);
    lstHeader1.add(header);
    lstHeader1.add(header1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    srConfigDTO.setStatus("A");
    lstConfig.add(srConfigDTO);
    lstConfig.add(srConfigDTO);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    List<SRCatalogDTO> lstCatalog = Mockito.spy(ArrayList.class);
    lstCatalog.add(srCatalogDTO);

    PowerMockito.when(srServiceManageRepository.getListSRConfigImport(any())).thenReturn(lstConfig);
    PowerMockito.when(srCatalogBusiness.getListSRCatalogDTO(any())).thenReturn(null);
    PowerMockito.when(srServiceManageRepository.getByConfigGroup(anyString()))
        .thenReturn(lstConfig);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000))
        .thenReturn(lstHeader1);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceArray_25() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("2");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.countryDisplay")).thenReturn("3");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("3");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    String filePath = "/temp";
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1", "1 (*)"};
    String[] header1 = new String[]{"1", "1", "1", "1 (*)", "1", "1 (*)"};
    header1[3] = "1";
    header1[4] = "1";
    header1[5] = "C";

    lstHeader.add(header);
    List<Object[]> lstHeader1 = Mockito.spy(ArrayList.class);
    lstHeader1.add(header);
    lstHeader1.add(header1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    srConfigDTO.setStatus("I");
    lstConfig.add(srConfigDTO);
    lstConfig.add(srConfigDTO);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCountry("1");
    List<SRCatalogDTO> lstCatalog = Mockito.spy(ArrayList.class);
    lstCatalog.add(srCatalogDTO);

    PowerMockito.when(srServiceManageRepository.getListSRConfigImport(any())).thenReturn(lstConfig);
    PowerMockito.when(srCatalogBusiness.getListSRCatalogDTO(any())).thenReturn(null);
    PowerMockito.when(srServiceManageRepository.getByConfigGroup(anyString()))
        .thenReturn(lstConfig);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000))
        .thenReturn(lstHeader1);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceArray(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceGroup_1() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());

    ResultInSideDto result = srServiceManageBusiness.importDataServiceGroup(null);
    assertEquals(result.getKey(), "FILE_IS_NULL");
  }

  @Test
  public void test_importDataServiceGroup_2() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";

    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceGroup(firstFile);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataServiceGroup_3() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1 (*)"};
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(header);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceGroup(firstFile);
    assertEquals(result.getKey(), "NODATA");
  }

  @Test
  public void test_importDataServiceGroup_4() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceGroup.title.export.list")).thenReturn("d");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.countryDisplay")).thenReturn("b");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("c");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("e");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1 (*)"};
    String[] header1 = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1 (*)"};
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(header);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 2000; i++) {
      lstData.add(header1);
    }
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000))
        .thenReturn(lstData);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceGroup(firstFile);
    assertEquals(result.getKey(), "DATA_OVER");
  }

  @Test
  public void test_importDataServiceGroup_5() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("SrServiceGroup.title.export.list")).thenReturn("d");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.countryDisplay")).thenReturn("b");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.automation")).thenReturn("c");
    PowerMockito.when(I18n.getLanguage("SrServiceArray.title.export.list")).thenReturn("e");
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    String[] header = new String[]{"1", "1 (*)", "1 (*)", "1 (*)", "1 (*)", "1 (*)"};
    String[] header1 = new String[]{"1", "1", "1 ", "1", "1", "C"};
    List<Object[]> lstHeader = Mockito.spy(ArrayList.class);
    lstHeader.add(header);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    lstData.add(header1);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigId(1L);
    srConfigDTO.setConfigName("1");
    srConfigDTO.setCountry("1");
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    lstConfig.add(srConfigDTO);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 7, 0, 5, 2))
        .thenReturn(lstHeader);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath), 0, 8, 0, 5, 1000))
        .thenReturn(lstData);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(srServiceManageRepository.getListConfigDTOGroup(any())).thenReturn(lstConfig);
    PowerMockito.when(srServiceManageRepository.getByConfigGroup("SERVICE_ARRAY"))
        .thenReturn(lstConfig);
    PowerMockito.when(srServiceManageRepository.getListConfigDTO(any())).thenReturn(lstConfig);
    PowerMockito.when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null))
        .thenReturn(lstCountryName);
    ResultInSideDto result = srServiceManageBusiness.importDataServiceGroup(firstFile);
    assertEquals(result.getKey(), "ERROR");
  }

  @Test
  public void test_getSRServiceArrayDetail() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    PowerMockito.when(srServiceManageRepository.getSRServiceArrayDetail(1L))
        .thenReturn(srConfigDTO);
    srServiceManageBusiness.getSRServiceArrayDetail(1L);
  }

  @Test
  public void test_getSRServiceGroupDetail() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    PowerMockito.when(srServiceManageRepository.getSRServiceGroupDetail(1L))
        .thenReturn(srConfigDTO);
    srServiceManageBusiness.getSRServiceGroupDetail(1L);
  }

  @Test
  public void test_deleteSRService() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto resultInSideDTO = Mockito.spy(ResultInSideDto.class);
    resultInSideDTO.setKey("SUCCESS");
    PowerMockito.when(srServiceManageRepository.deleteSRService(any())).thenReturn(resultInSideDTO);
    ResultInSideDto result = srServiceManageBusiness.deleteSRService(1L);
    assertEquals(result.getKey(), "SUCCESS");

  }

  @Test
  public void test_getListSearchSRServiceGroup() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setParentCode("1");
    srConfigDTO.setCountry("1");
    List<SRConfigDTO> list = Mockito.spy(ArrayList.class);
    list.add(srConfigDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(list);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    List<SRCatalogDTO> srCatalogDTOS = Mockito.spy(ArrayList.class);
    srCatalogDTOS.add(srCatalogDTO);
    PowerMockito.when(srCatalogRepository.getListSRCatalogDTO(any())).thenReturn(srCatalogDTOS);
    PowerMockito.when(srServiceManageRepository.getListSearchSRServiceGroup(any()))
        .thenReturn(datatable);
    Datatable result = srServiceManageBusiness.getListSearchSRServiceGroup(srConfigDTO);
    assertEquals(datatable.getPages(), result.getPages());

  }

  @Test
  public void test_insertOrUpdateServiceGroup() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    srConfigDTO.setConfigId(0L);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByCode(any())).thenReturn(true);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByName(any())).thenReturn(true);
    ResultInSideDto result = srServiceManageBusiness.insertOrUpdateServiceGroup(srConfigDTO);
    assertEquals(result.getKey(), "DUPLICATE");
  }

  @Test
  public void test_insertOrUpdateServiceGroup_1() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    srConfigDTO.setConfigId(0L);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByCode(any()))
        .thenReturn(false);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByName(any())).thenReturn(true);
    ResultInSideDto result = srServiceManageBusiness.insertOrUpdateServiceGroup(srConfigDTO);
    assertEquals(result.getKey(), "DUPLICATE");
  }

  @Test
  public void test_insertOrUpdateServiceGroup_2() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    srConfigDTO.setConfigId(0L);
    srConfigDTO.setStatus("I");
    PowerMockito.when(srServiceManageRepository.getSRServiceArrayByDTO(any()))
        .thenReturn(srConfigDTO);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByCode(any()))
        .thenReturn(false);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByName(any()))
        .thenReturn(false);
    ResultInSideDto result = srServiceManageBusiness.insertOrUpdateServiceGroup(srConfigDTO);
    assertEquals(result.getKey(), "DUPLICATE");
  }

  @Test
  public void test_insertOrUpdateServiceGroup_3() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    srConfigDTO.setConfigId(0L);
    srConfigDTO.setStatus("I");
    SRConfigDTO dtoUpdate = Mockito.spy(SRConfigDTO.class);
    dtoUpdate.setConfigCode("2");
    dtoUpdate.setConfigName("2");
    dtoUpdate.setConfigId(0L);
    dtoUpdate.setStatus("A");
    PowerMockito.when(srServiceManageRepository.getSRServiceArrayByDTO(any()))
        .thenReturn(dtoUpdate);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByCode(any()))
        .thenReturn(false);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByName(any()))
        .thenReturn(false);
    ResultInSideDto result = srServiceManageBusiness.insertOrUpdateServiceGroup(srConfigDTO);
    assertEquals(result.getKey(), "DUPLICATE");
  }

  @Test
  public void test_insertOrUpdateServiceGroup_4() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    srConfigDTO.setConfigId(0L);
    srConfigDTO.setStatus("I");
    SRConfigDTO dtoUpdate = Mockito.spy(SRConfigDTO.class);
    dtoUpdate.setConfigCode("2");
    dtoUpdate.setConfigName("2");
    dtoUpdate.setConfigId(0L);
    dtoUpdate.setStatus("A");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(srServiceManageRepository.insertOrUpdateService(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(srServiceManageRepository.getSRServiceArrayByDTO(any())).thenReturn(null);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByCode(any()))
        .thenReturn(false);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByName(any()))
        .thenReturn(false);
    ResultInSideDto result = srServiceManageBusiness.insertOrUpdateServiceGroup(srConfigDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void test_insertOrUpdateServiceGroup_5() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    srConfigDTO.setConfigId(1L);
    srConfigDTO.setStatus("I");
    SRConfigDTO dtoUpdate = Mockito.spy(SRConfigDTO.class);
    dtoUpdate.setConfigCode("2");
    dtoUpdate.setConfigName("2");
    dtoUpdate.setConfigId(0L);
    dtoUpdate.setStatus("A");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(srServiceManageRepository.getSRServiceGroupDetail(any()))
        .thenReturn(dtoUpdate);
    PowerMockito.when(srServiceManageRepository.insertOrUpdateService(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByCode(any()))
        .thenReturn(false);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByName(any())).thenReturn(true);
    ResultInSideDto result = srServiceManageBusiness.insertOrUpdateServiceGroup(srConfigDTO);
    assertEquals(result.getKey(), "DUPLICATE");
  }

  @Test
  public void test_insertOrUpdateServiceGroup_6() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    srConfigDTO.setConfigId(1L);
    srConfigDTO.setStatus("I");
    SRConfigDTO dtoUpdate = Mockito.spy(SRConfigDTO.class);
    dtoUpdate.setConfigCode("2");
    dtoUpdate.setConfigName("2");
    dtoUpdate.setConfigId(0L);
    dtoUpdate.setStatus("A");
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    List<SRConfigDTO> list = Mockito.spy(ArrayList.class);
    list.add(srConfigDTO);
    PowerMockito.when(srServiceManageRepository.getSrConfigExistedByName(any())).thenReturn(list);
    PowerMockito.when(srServiceManageRepository.getSRServiceGroupDetail(any()))
        .thenReturn(dtoUpdate);
    PowerMockito.when(srServiceManageRepository.insertOrUpdateService(any()))
        .thenReturn(resultInSideDto);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByCode(any()))
        .thenReturn(false);
    PowerMockito.when(srServiceManageRepository.checkSrConfigExistedByName(any()))
        .thenReturn(false);
    ResultInSideDto result = srServiceManageBusiness.insertOrUpdateServiceGroup(srConfigDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void test_getSRServiceArrayByDTO() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    PowerMockito.when(srServiceManageRepository.getSRServiceArrayByDTO(any()))
        .thenReturn(srConfigDTO);
    srServiceManageBusiness.getSRServiceArrayByDTO(srConfigDTO);
  }

  @Test
  public void test_checkEnableGroup() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    List<SRConfigDTO> list = Mockito.spy(ArrayList.class);
    list.add(srConfigDTO);
    PowerMockito.when(srServiceManageRepository.checkEnableGroup(any())).thenReturn(list);
    srServiceManageBusiness.checkEnableGroup(srConfigDTO);
  }

  @Test
  public void test_insertOrUpdateSRConfigService() {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    PowerMockito.when(srServiceManageRepository.insertOrUpdateService(any()))
        .thenReturn(resultInSideDto);
    ResultInSideDto result = srServiceManageBusiness.insertOrUpdateSRConfigService(srConfigDTO);
    assertEquals(result.getKey(), "SUCCESS");
  }

  @Test
  public void test_importDataServiceArray_26() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
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

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] objecttest1 = new Object[]{
        "1", "1 (*)", "1 (*)","1 (*)", "1", "1 (*)"
    };
    headerList.add(objecttest1);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    Object[] objecttest2 = new Object[]{
        "1", "1", "1", "1", "1", "C"
    };
    lstData.add(objecttest2);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 7, 0, 5, 2
        )
    ).thenReturn(headerList);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 8, 0, 5, 1000
        )
    ).thenReturn(lstData);

    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    lstConfig.add(srConfigDTO);

    PowerMockito.when(
        catLocationBusiness
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountryName);
    PowerMockito.when(
        srServiceManageRepository
            .getByConfigGroup(anyString())
    ).thenReturn(lstConfig);

    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        srServiceManageRepository.insertOrUpdateService(any())
    ).thenReturn(result);

    ResultInSideDto actual = srServiceManageBusiness.importDataServiceArray(multipartFile);
    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void test_importDataServiceArray_27() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
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

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] objecttest1 = new Object[]{
        "1", "1 (*)", "1 (*)","1 (*)", "1", "1 (*)"
    };
    headerList.add(objecttest1);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    Object[] objecttest2 = new Object[]{
        "1", "1", "1", "1", "1", "D"
    };
    lstData.add(objecttest2);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 7, 0, 5, 2
        )
    ).thenReturn(headerList);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 8, 0, 5, 1000
        )
    ).thenReturn(lstData);

    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigId(1L);
    srConfigDTO.setConfigName("1");
    srConfigDTO.setStatus("A");
    lstConfig.add(srConfigDTO);

    PowerMockito.when(
        catLocationBusiness
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountryName);
    PowerMockito.when(
        srServiceManageRepository
            .getByConfigGroup(anyString())
    ).thenReturn(lstConfig);
    PowerMockito.when(
        srServiceManageRepository
            .getListSRConfigImport(any())
    ).thenReturn(lstConfig);

    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        srServiceManageRepository.insertOrUpdateService(any())
    ).thenReturn(result);

    ResultInSideDto actual = srServiceManageBusiness.importDataServiceArray(multipartFile);
    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void test_importDataServiceArray_28() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
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

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] objecttest1 = new Object[]{
        "1", "1 (*)", "1 (*)","1 (*)", "1", "1 (*)"
    };
    headerList.add(objecttest1);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    Object[] objecttest2 = new Object[]{
        "1", "1", "1", "1", "1", "C"
    };
    lstData.add(objecttest2);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 7, 0, 5, 2
        )
    ).thenReturn(headerList);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 8, 0, 5, 1000
        )
    ).thenReturn(lstData);

    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigId(1L);
    srConfigDTO.setConfigName("1");
    srConfigDTO.setStatus("I");
    SRConfigDTO srConfigDTO1 = Mockito.spy(SRConfigDTO.class);
    srConfigDTO1.setConfigCode("1");
    srConfigDTO1.setConfigId(1L);
    srConfigDTO1.setConfigName("1");
    srConfigDTO1.setStatus("I");
    lstConfig.add(srConfigDTO);
    lstConfig.add(srConfigDTO1);

    PowerMockito.when(
        catLocationBusiness
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountryName);
    PowerMockito.when(
        srServiceManageRepository
            .getByConfigGroup(anyString())
    ).thenReturn(lstConfig);
    PowerMockito.when(
        srServiceManageRepository
            .getListSRConfigImport(any())
    ).thenReturn(lstConfig);

    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        srServiceManageRepository.insertOrUpdateService(any())
    ).thenReturn(result);

    ResultInSideDto actual = srServiceManageBusiness.importDataServiceArray(multipartFile);
    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void test_importDataServiceArray_29() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
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

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] objecttest1 = new Object[]{
        "1", "1 (*)", "1 (*)","1 (*)", "1", "1 (*)"
    };
    headerList.add(objecttest1);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    Object[] objecttest2 = new Object[]{
        "1", "1", "1", "1", "1", "D"
    };
    lstData.add(objecttest2);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 7, 0, 5, 2
        )
    ).thenReturn(headerList);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 8, 0, 5, 1000
        )
    ).thenReturn(lstData);

    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigId(1L);
    srConfigDTO.setConfigName("1");
    srConfigDTO.setStatus("A");
    SRConfigDTO srConfigDTO1 = Mockito.spy(SRConfigDTO.class);
    srConfigDTO1.setConfigCode("1");
    srConfigDTO1.setConfigId(1L);
    srConfigDTO1.setConfigName("1");
    srConfigDTO1.setStatus("A");
    lstConfig.add(srConfigDTO);
    lstConfig.add(srConfigDTO1);

    PowerMockito.when(
        catLocationBusiness
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountryName);
    PowerMockito.when(
        srServiceManageRepository
            .getByConfigGroup(anyString())
    ).thenReturn(lstConfig);
    PowerMockito.when(
        srServiceManageRepository
            .getListSRConfigImport(any())
    ).thenReturn(lstConfig);

    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        srServiceManageRepository.insertOrUpdateService(any())
    ).thenReturn(result);

    ResultInSideDto actual = srServiceManageBusiness.importDataServiceArray(multipartFile);
    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }

  @Test
  public void test_importDataServiceArray_30() throws Exception {
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);

    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
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

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    PowerMockito.when(
        ticketProvider.getUserToken()
    ).thenReturn(userToken);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] objecttest1 = new Object[]{
        "1", "1 (*)", "1 (*)","1 (*)", "1", "1 (*)"
    };
    headerList.add(objecttest1);
    List<Object[]> lstData = Mockito.spy(ArrayList.class);
    Object[] objecttest2 = new Object[]{
        "1", "1", "1", "1", "1", "D"
    };
    lstData.add(objecttest2);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 7, 0, 5, 2
        )
    ).thenReturn(headerList);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(
            fileImport, 0, 8, 0, 5, 1000
        )
    ).thenReturn(lstData);

    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    itemDataCRInside.setDisplayStr("1");
    lstCountryName.add(itemDataCRInside);
    List<SRConfigDTO> lstConfig = Mockito.spy(ArrayList.class);
    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigId(1L);
    srConfigDTO.setConfigName("1");
    srConfigDTO.setStatus("A");
    SRConfigDTO srConfigDTO1 = Mockito.spy(SRConfigDTO.class);
    srConfigDTO1.setConfigCode("1");
    srConfigDTO1.setConfigId(1L);
    srConfigDTO1.setConfigName("1");
    srConfigDTO1.setStatus("I");
    lstConfig.add(srConfigDTO);
    lstConfig.add(srConfigDTO1);

    PowerMockito.when(
        catLocationBusiness
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountryName);
    PowerMockito.when(
        srServiceManageRepository
            .getByConfigGroup(anyString())
    ).thenReturn(lstConfig);
    PowerMockito.when(
        srServiceManageRepository
            .getListSRConfigImport(any())
    ).thenReturn(lstConfig);

    ResultInSideDto result = Mockito.spy(ResultInSideDto.class);
    result.setKey(RESULT.SUCCESS);
    PowerMockito.when(
        srServiceManageRepository.insertOrUpdateService(any())
    ).thenReturn(result);

    ResultInSideDto actual = srServiceManageBusiness.importDataServiceArray(multipartFile);
    Assert.assertEquals(RESULT.SUCCESS, actual.getKey());
  }
}
