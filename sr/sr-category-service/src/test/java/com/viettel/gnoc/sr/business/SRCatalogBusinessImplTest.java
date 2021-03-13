package com.viettel.gnoc.sr.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.when;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.DataHistoryChange;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRCatalogChildDTO;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRFilesDTO;
import com.viettel.gnoc.sr.dto.SRFlowExecuteDTO;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import com.viettel.gnoc.sr.repository.SRCatalogChildRepository;
import com.viettel.gnoc.sr.repository.SRCatalogRepository;
import com.viettel.gnoc.sr.repository.SRConfigRepository;
import com.viettel.gnoc.sr.repository.SRRoleUserRepository;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import javax.jws.soap.SOAPBinding.Use;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SRCatalogBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class SRCatalogBusinessImplTest {

  @InjectMocks
  SRCatalogBusinessImpl srCatalogBusiness;

  @Mock
  SRCatalogRepository srCatalogRepository;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Mock
  GnocFileRepository gnocFileRepository;

  @Mock
  SRCatalogChildRepository srCatalogChildRepository;

  @Mock
  SRConfigRepository srConfigRepository;

  @Mock
  SRRoleUserRepository srRoleUserRepository;

  @Mock
  UnitRepository unitRepository;

  @Mock
  SRConfigBusiness srConfigBusiness;

  @Mock
  UnitBusiness unitBusiness;

  @Mock
  SRRoleBusiness srRoleBusiness;

  @Mock
  SRFlowExecuteBusiness srFlowExecuteBusiness;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();


  @Test
  public void test_getListSRCatalogPage() {
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceId(2L);
    List<SRCatalogDTO> lstDatatable = Mockito.spy(ArrayList.class);
    lstDatatable.add(srCatalogDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setPages(1);
    datatable.setTotal(10);
    datatable.setData(lstDatatable);

    SRCatalogDTO srCatalogNotUsingDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogNotUsingDTO.setServiceId(2L);
    List<SRCatalogDTO> srCatalogNotUsingList = Mockito.spy(ArrayList.class);
    srCatalogNotUsingList.add(srCatalogNotUsingDTO);

    when(srCatalogRepository.getListSRCatalogPage(any())).thenReturn(datatable);
    when(srCatalogRepository.getListSRCatalogNotUsing(any())).thenReturn(srCatalogNotUsingList);
    srCatalogBusiness.getListSRCatalogPage(srCatalogDTO);
  }

  @Test
  public void test_getListServiceChild() {
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    List<SRCatalogDTO> lst = Mockito.spy(ArrayList.class);

    when(srCatalogRepository.getListServiceChild(any())).thenReturn(lst);
    List<SRCatalogDTO> result = srCatalogBusiness.getListServiceChild(srCatalogDTO);
    assertEquals(lst.size(), result.size());
  }

  @Test
  public void test_getListSRCatalog() throws Exception {
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    List<SRCatalogDTO> data = new ArrayList<>();
    when(srCatalogRepository.getListSRCatalog(any())).thenReturn(data);
    setFinalStatic(SRCatalogBusinessImpl.class.getDeclaredField("log"), logger);
    List<SRCatalogDTO> result = srCatalogBusiness.getListSRCatalog(new SRCatalogDTO());
    assertEquals(data, result);
  }

  @Test
  public void test_delete_01() throws Exception {
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setUserName("thanhlv12");
    userToken.setUserID(999999l);
    srCatalogDTO.setServiceId(1L);
    SRCatalogBusinessImpl srCatalogBusinessInline = Mockito.spy(srCatalogBusiness);
    PowerMockito.doReturn(srCatalogDTO).when(srCatalogBusinessInline).getDetail(anyLong());
    DataHistoryChange dataHistoryChange = Mockito.spy(DataHistoryChange.class);
    dataHistoryChange.setType("SR_CATALOG");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(TicketProvider.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    PowerMockito.when(resultInSideDto.getId()).thenReturn(10L);
    PowerMockito.when(commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange)).thenReturn(resultInSideDto);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    PowerMockito.when(srCatalogRepository.delete(any())).thenReturn(resultInSideDto);
    ResultInSideDto result = srCatalogBusinessInline.delete(123L);
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void test_getDetail() {
    Long serviceId = 2L;
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setExecutionUnit("2");
    srCatalogDTO.setAutoCreateSR("22");

    SRConfigDTO srToOutsideService = Mockito.spy(SRConfigDTO.class);
    srToOutsideService.setConfigGroup("2");

    SRRoleUserInSideDTO srRoleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    List<SRRoleUserInSideDTO> srRoleUserDTOList = Mockito.spy(ArrayList.class);
    srRoleUserDTOList.add(srRoleUserInSideDTO);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setBusinessCode("SR_CATALOG");
    gnocFileDto.setBusinessId(123L);
    List<GnocFileDto> gnocFileDtos = Mockito.spy(ArrayList.class);
    gnocFileDtos.add(gnocFileDto);
    PowerMockito.when(gnocFileRepository.getListGnocFileForSR(any())).thenReturn(gnocFileDtos);

    SRCatalogDTO srCatalogDTO1 = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO1.setServiceIdStr("1,2");
    List<SRCatalogDTO> srCatalogDTOList = Mockito.spy(ArrayList.class);
    srCatalogDTOList.add(srCatalogDTO1);

    when(srCatalogRepository.getDetail(anyLong())).thenReturn(srCatalogDTO);
    when(srConfigRepository.getConfigGroupByConfigCode(any(), anyString(), anyString(), any()))
        .thenReturn(srToOutsideService);
    when(srRoleUserRepository.getListSRRoleUserByUnitId(any())).thenReturn(srRoleUserDTOList);
    when(srCatalogRepository.getListRoleCodeByServiceCode(any())).thenReturn(srCatalogDTOList);
    srCatalogBusiness.getDetail(serviceId);
  }

  @Test
  public void test_insert() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    Logger logger = PowerMockito.mock(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    List<MultipartFile> srFilesList = Mockito.spy(ArrayList.class);
    srFilesList.add(multipartFile);
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setIndexFile(null);
    gnocFileDto.setMappingId(1L);
    List<GnocFileDto> lst = Mockito.spy(ArrayList.class);
    lst.add(gnocFileDto);
    SRCatalogChildDTO srCatalogChildDTO = Mockito.spy(SRCatalogChildDTO.class);
    srCatalogChildDTO.setChildId(2L);
    List<SRCatalogChildDTO> lstCatalogChild = Mockito.spy(ArrayList.class);
    lstCatalogChild.add(srCatalogChildDTO);
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceCode("aaa");
    srCatalogDTO.setSrToOutsideService("22");
    srCatalogDTO.setOutsideServiceToSr("22");
    srCatalogDTO.setSrCatalogChildDTOList(lstCatalogChild);
    srCatalogDTO.setGnocFileDtos(lst);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    SRRoleUserInSideDTO srRoleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserInSideDTO.setUnitId(2L);
    List<SRRoleUserInSideDTO> srRoleUserDTOList = Mockito.spy(ArrayList.class);
    srRoleUserDTOList.add(srRoleUserInSideDTO);
    srRoleUserDTOList.add(srRoleUserInSideDTO);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(srCatalogRepository.add(any())).thenReturn(resultInSideDto);
    ResultInSideDto resultFileDataOld = Mockito.spy(ResultInSideDto.class);
    when(srCatalogRepository.addSRFile(any())).thenReturn(resultFileDataOld);
    ResultInSideDto resultInSideDtocfg = Mockito.spy(ResultInSideDto.class);
    resultInSideDtocfg.setKey("SUCCESS");
    when(srConfigRepository.add(any())).thenReturn(resultInSideDtocfg);
    when(unitRepository.findUnitById(any())).thenReturn(unitToken);
    when(srCatalogDTO.getSrRoleUserDTOList()).thenReturn(srRoleUserDTOList);
    SRFilesDTO srFileUpdate = Mockito.spy(SRFilesDTO.class);
    srFileUpdate.setFilePath("1");
    when(srCatalogRepository.findSRFile(anyLong())).thenReturn(srFileUpdate);
    srCatalogBusiness.insert(srFilesList, srCatalogDTO);
  }

  @Test
  public void test_update() throws Exception {
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    List<MultipartFile> lst = Mockito.spy(ArrayList.class);
    lst.add(multipartFile);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setIsUpdateAllUnit("0");
    srCatalogDTO.setOutsideServiceToSr("1");
    srCatalogDTO.setOutSideSrConfig(srConfigDTO);
    srCatalogDTO.setSrToOutsideService("1");
    srCatalogDTO.setSrConfig(srConfigDTO);
    srCatalogDTO.setServiceId(1L);
    srCatalogDTO.setIsUpdateAllUnit("0");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    SRCatalogBusinessImpl srCatalogBusinessInline = Mockito.spy(srCatalogBusiness);
    PowerMockito.doReturn(srCatalogDTO).when(srCatalogBusinessInline).getDetail(anyLong());

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    when(unitRepository.findUnitById(any())).thenReturn(unitToken);

    SRRoleUserInSideDTO srRoleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserInSideDTO.setUnitId(1L);
    List<SRRoleUserInSideDTO> srRoleUserDTOList = Mockito.spy(ArrayList.class);
    srRoleUserDTOList.add(srRoleUserInSideDTO);
    when(srCatalogDTO.getSrRoleUserDTOList()).thenReturn(srRoleUserDTOList);

    SRCatalogDTO srCatalogDTO1 = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO1.setExecutionUnit("1");
    srCatalogDTO1.setServiceIdStr("1");
    List<SRCatalogDTO> srCatalogList = Mockito.spy(ArrayList.class);
    srCatalogList.add(srCatalogDTO1);
    when(srCatalogRepository.getListRoleCodeByServiceCode(any())).thenReturn(srCatalogList);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(srCatalogRepository.add(any())).thenReturn(resultInSideDto);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("SUCCESS");
    when(srConfigRepository.add(any())).thenReturn(resultInSideDto1);

    List<GnocFileDto> coGnocFileDtos = Mockito.spy(ArrayList.class);
    when(srCatalogDTO.getGnocFileDtos()).thenReturn(coGnocFileDtos);

    SRCatalogChildDTO srCatalogChildDTO = Mockito.spy(SRCatalogChildDTO.class);
    List<SRCatalogChildDTO> srCatalogChildDTOList = Mockito.spy(ArrayList.class);
    srCatalogChildDTOList.add(srCatalogChildDTO);
    when(srCatalogDTO.getSrCatalogChildDTOList()).thenReturn(srCatalogChildDTOList);

    SRCatalogChildDTO srCatalogChildDTODelete = Mockito.spy(SRCatalogChildDTO.class);
    srCatalogChildDTODelete.setChildId(1L);
    List<SRCatalogChildDTO> lstSRCatalogChildDelete = Mockito.spy(ArrayList.class);
    lstSRCatalogChildDelete.add(srCatalogChildDTODelete);
    when(srCatalogChildRepository.getListCatalogChild(any())).thenReturn(lstSRCatalogChildDelete);

    ResultInSideDto resultInSideDtoDelete = Mockito.spy(ResultInSideDto.class);
    when(srCatalogChildRepository.delete(any())).thenReturn(resultInSideDtoDelete);

    ResultInSideDto resultInSideDtoAdd = Mockito.spy(ResultInSideDto.class);
    when(srCatalogChildRepository.add(any())).thenReturn(resultInSideDtoAdd);

    SRConfigDTO srConfigDTOOld = Mockito.spy(SRConfigDTO.class);
    when(srConfigRepository.getDetail(any())).thenReturn(srConfigDTOOld);

    srCatalogBusinessInline.update(lst, srCatalogDTO);
  }

  @Test
  public void test_update1() throws Exception {
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    List<MultipartFile> lst = Mockito.spy(ArrayList.class);
    lst.add(multipartFile);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setIsUpdateAllUnit("0");
    srCatalogDTO.setOutsideServiceToSr("1");
    srCatalogDTO.setOutSideSrConfig(srConfigDTO);
    srCatalogDTO.setSrToOutsideService("1");
    srCatalogDTO.setSrConfig(srConfigDTO);
    srCatalogDTO.setServiceId(1L);
    srCatalogDTO.setIsUpdateAllUnit("0");

    SRCatalogBusinessImpl srCatalogBusinessInline = Mockito.spy(srCatalogBusiness);
    PowerMockito.doReturn(srCatalogDTO).when(srCatalogBusinessInline).getDetail(anyLong());

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    when(unitRepository.findUnitById(any())).thenReturn(unitToken);

    SRRoleUserInSideDTO srRoleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserInSideDTO.setUnitId(3L);
    List<SRRoleUserInSideDTO> srRoleUserDTOList = Mockito.spy(ArrayList.class);
    srRoleUserDTOList.add(srRoleUserInSideDTO);
    when(srCatalogDTO.getSrRoleUserDTOList()).thenReturn(srRoleUserDTOList);

    SRCatalogDTO srCatalogDTO1 = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO1.setExecutionUnit("1");
    srCatalogDTO1.setServiceIdStr("1");
    List<SRCatalogDTO> srCatalogList = Mockito.spy(ArrayList.class);
    srCatalogList.add(srCatalogDTO1);
    when(srCatalogRepository.getListRoleCodeByServiceCode(any())).thenReturn(srCatalogList);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(srCatalogRepository.add(any())).thenReturn(resultInSideDto);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("SUCCESS");
    when(srConfigRepository.add(any())).thenReturn(resultInSideDto1);

    List<GnocFileDto> coGnocFileDtos = Mockito.spy(ArrayList.class);
    when(srCatalogDTO.getGnocFileDtos()).thenReturn(coGnocFileDtos);

    SRCatalogChildDTO srCatalogChildDTO = Mockito.spy(SRCatalogChildDTO.class);
    List<SRCatalogChildDTO> srCatalogChildDTOList = Mockito.spy(ArrayList.class);
    srCatalogChildDTOList.add(srCatalogChildDTO);
    when(srCatalogDTO.getSrCatalogChildDTOList()).thenReturn(srCatalogChildDTOList);

    SRCatalogChildDTO srCatalogChildDTODelete = Mockito.spy(SRCatalogChildDTO.class);
    srCatalogChildDTODelete.setChildId(1L);
    List<SRCatalogChildDTO> lstSRCatalogChildDelete = Mockito.spy(ArrayList.class);
    lstSRCatalogChildDelete.add(srCatalogChildDTODelete);
    when(srCatalogChildRepository.getListCatalogChild(any())).thenReturn(lstSRCatalogChildDelete);

    ResultInSideDto resultInSideDtoDelete = Mockito.spy(ResultInSideDto.class);
    when(srCatalogChildRepository.delete(any())).thenReturn(resultInSideDtoDelete);

    ResultInSideDto resultInSideDtoAdd = Mockito.spy(ResultInSideDto.class);
    when(srCatalogChildRepository.add(any())).thenReturn(resultInSideDtoAdd);

    ResultInSideDto resultInSideDtoDe = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoDe.setKey("SUCCESS");
    when(srCatalogRepository.delete(any())).thenReturn(resultInSideDtoDe);

    SRConfigDTO srConfigDTOOld = Mockito.spy(SRConfigDTO.class);
    when(srConfigRepository.getDetail(any())).thenReturn(srConfigDTOOld);

    srCatalogBusinessInline.update(lst, srCatalogDTO);
  }

  @Test
  public void test_update2() throws Exception {
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    List<MultipartFile> lst = Mockito.spy(ArrayList.class);
    lst.add(multipartFile);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setIsUpdateAllUnit("2");
    srCatalogDTO.setOutsideServiceToSr("1");
    srCatalogDTO.setOutSideSrConfig(srConfigDTO);
    srCatalogDTO.setSrToOutsideService("1");
    srCatalogDTO.setSrConfig(srConfigDTO);
    srCatalogDTO.setServiceId(1L);

    SRCatalogBusinessImpl srCatalogBusinessInline = Mockito.spy(srCatalogBusiness);
    PowerMockito.doReturn(srCatalogDTO).when(srCatalogBusinessInline).getDetail(anyLong());

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    when(unitRepository.findUnitById(any())).thenReturn(unitToken);

    SRRoleUserInSideDTO srRoleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserInSideDTO.setUnitId(1L);
    List<SRRoleUserInSideDTO> srRoleUserDTOList = Mockito.spy(ArrayList.class);
    srRoleUserDTOList.add(srRoleUserInSideDTO);
    srCatalogDTO.setSrRoleUserDTOList(srRoleUserDTOList);
    when(srCatalogDTO.getSrRoleUserDTOList()).thenReturn(srRoleUserDTOList);

    SRCatalogDTO srCatalogDTO1 = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO1.setExecutionUnit("1");
    srCatalogDTO1.setServiceIdStr("1");
    srCatalogDTO1.setIsUpdateAllUnit("1");
    List<SRCatalogDTO> srCatalogList = Mockito.spy(ArrayList.class);
    srCatalogList.add(srCatalogDTO1);
    when(srCatalogRepository.getListRoleCodeByServiceCode(any())).thenReturn(srCatalogList);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(srCatalogRepository.add(any())).thenReturn(resultInSideDto);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("SUCCESS");
    when(srConfigRepository.add(any())).thenReturn(resultInSideDto1);

    List<GnocFileDto> coGnocFileDtos = Mockito.spy(ArrayList.class);
    when(srCatalogDTO.getGnocFileDtos()).thenReturn(coGnocFileDtos);

    SRCatalogChildDTO srCatalogChildDTO = Mockito.spy(SRCatalogChildDTO.class);
    List<SRCatalogChildDTO> srCatalogChildDTOList = Mockito.spy(ArrayList.class);
    srCatalogChildDTOList.add(srCatalogChildDTO);
    when(srCatalogDTO.getSrCatalogChildDTOList()).thenReturn(srCatalogChildDTOList);

    SRCatalogChildDTO srCatalogChildDTODelete = Mockito.spy(SRCatalogChildDTO.class);
    srCatalogChildDTODelete.setChildId(1L);
    List<SRCatalogChildDTO> lstSRCatalogChildDelete = Mockito.spy(ArrayList.class);
    lstSRCatalogChildDelete.add(srCatalogChildDTODelete);
    when(srCatalogChildRepository.getListCatalogChild(any())).thenReturn(lstSRCatalogChildDelete);

    ResultInSideDto resultInSideDtoDelete = Mockito.spy(ResultInSideDto.class);
    when(srCatalogChildRepository.delete(any())).thenReturn(resultInSideDtoDelete);

    ResultInSideDto resultInSideDtoAdd = Mockito.spy(ResultInSideDto.class);
    when(srCatalogChildRepository.add(any())).thenReturn(resultInSideDtoAdd);

    SRConfigDTO srConfigDTOOld = Mockito.spy(SRConfigDTO.class);
    srConfigDTOOld.setConfigId(1L);
    srConfigDTOOld.setConfigGroup("1,1,1");
    srConfigDTOOld.setSrCfgServiceIds("1,1,1");
    when(srConfigRepository.getDetail(any())).thenReturn(srConfigDTOOld);
    when(srConfigRepository.add(any())).thenReturn(resultInSideDto);
    SRFilesDTO srFilesDTO = Mockito.spy(SRFilesDTO.class);
    srFilesDTO.setFileId(1L);
    List<SRFilesDTO> lstFile = Mockito.spy(ArrayList.class);
    lstFile.add(srFilesDTO);
    when(srCatalogRepository.findSrFilesByServiceIds(any())).thenReturn(lstFile);
    srCatalogBusinessInline.update(lst, srCatalogDTO);
  }

  @Test
  public void test_update3() throws Exception {
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    List<MultipartFile> lst = Mockito.spy(ArrayList.class);
    lst.add(multipartFile);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setIsUpdateAllUnit("1");
    srCatalogDTO.setOutsideServiceToSr("1");
    srCatalogDTO.setOutSideSrConfig(null);
    srCatalogDTO.setSrToOutsideService("1");
    srCatalogDTO.setSrConfig(null);
    srCatalogDTO.setServiceId(1L);
    srCatalogDTO.setServiceCode("1");
    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setFileName("1");
    gnocFileDto.setMappingId(1L);
    List<GnocFileDto> gnocFileDtoList = Mockito.spy(ArrayList.class);
    gnocFileDtoList.add(gnocFileDto);
    srCatalogDTO.setGnocFileDtos(gnocFileDtoList);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    SRCatalogBusinessImpl srCatalogBusinessInline = Mockito.spy(srCatalogBusiness);
    PowerMockito.doReturn(srCatalogDTO).when(srCatalogBusinessInline).getDetail(anyLong());

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    when(unitRepository.findUnitById(any())).thenReturn(unitToken);

    SRRoleUserInSideDTO srRoleUserInSideDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserInSideDTO.setUnitId(1L);
    List<SRRoleUserInSideDTO> srRoleUserDTOList = Mockito.spy(ArrayList.class);
    srRoleUserDTOList.add(srRoleUserInSideDTO);
    when(srCatalogDTO.getSrRoleUserDTOList()).thenReturn(srRoleUserDTOList);

    SRCatalogDTO srCatalogDTO1 = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO1.setExecutionUnit("1");
    srCatalogDTO1.setServiceIdStr("1");
    srCatalogDTO1.setIsUpdateAllUnit("1");
    List<SRCatalogDTO> srCatalogList = Mockito.spy(ArrayList.class);
    srCatalogList.add(srCatalogDTO1);
    when(srCatalogRepository.getListRoleCodeByServiceCode(any())).thenReturn(srCatalogList);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(srCatalogRepository.add(any())).thenReturn(resultInSideDto);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("SUCCESS");
    when(srConfigRepository.add(any())).thenReturn(resultInSideDto1);

    List<GnocFileDto> coGnocFileDtos = Mockito.spy(ArrayList.class);
    when(srCatalogDTO.getGnocFileDtos()).thenReturn(coGnocFileDtos);

    SRCatalogChildDTO srCatalogChildDTO = Mockito.spy(SRCatalogChildDTO.class);
    List<SRCatalogChildDTO> srCatalogChildDTOList = Mockito.spy(ArrayList.class);
    srCatalogChildDTOList.add(srCatalogChildDTO);
    when(srCatalogDTO.getSrCatalogChildDTOList()).thenReturn(srCatalogChildDTOList);

    SRCatalogChildDTO srCatalogChildDTODelete = Mockito.spy(SRCatalogChildDTO.class);
    srCatalogChildDTODelete.setChildId(1L);
    List<SRCatalogChildDTO> lstSRCatalogChildDelete = Mockito.spy(ArrayList.class);
    lstSRCatalogChildDelete.add(srCatalogChildDTODelete);
    when(srCatalogChildRepository.getListCatalogChild(any())).thenReturn(lstSRCatalogChildDelete);

    ResultInSideDto resultInSideDtoDelete = Mockito.spy(ResultInSideDto.class);
    when(srCatalogChildRepository.delete(any())).thenReturn(resultInSideDtoDelete);

    ResultInSideDto resultInSideDtoAdd = Mockito.spy(ResultInSideDto.class);
    when(srCatalogChildRepository.add(any())).thenReturn(resultInSideDtoAdd);
    SRFilesDTO srFilesDTO = Mockito.spy(SRFilesDTO.class);
    List<SRFilesDTO> lstFile = Mockito.spy(ArrayList.class);
    lstFile.add(srFilesDTO);
    when(srCatalogRepository.findSrFilesByServiceIds(any())).thenReturn(lstFile);

    srCatalogBusinessInline.update(lst, srCatalogDTO);
  }

  @Test
  public void test_setMapCountryName() {
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(2L);
    itemDataCRInside.setDisplayStr("test");
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstCountryName);
    srCatalogBusiness.setMapCountryName();
  }

  @Test
  public void test_getCatalogTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("srCatalog.title")).thenReturn("Import SR Catalog");
    PowerMockito.when(I18n.getLanguage("srCatalog.serviceArrayGroup"))
        .thenReturn("Danh sách mảng - Nhóm dịch vụ");
    PowerMockito.when(I18n.getLanguage("srCatalog.UnitExecute"))
        .thenReturn("Danh sách đơn vị xử lý");
    PowerMockito.when(I18n.getLanguage("srCatalog.RoleExecute")).thenReturn("Danh sách nhóm xử lý");
    PowerMockito.when(I18n.getLanguage("srCatalog.FlowExecute")).thenReturn("Luồng xử lý");
    PowerMockito.when(I18n.getLanguage("srCatalog.SystemService"))
        .thenReturn("Dịch vụ giao tiếp hệ thống");
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(2L);
    itemDataCRInside.setDisplayStr("test");
    List<ItemDataCRInside> countryNameList = Mockito.spy(ArrayList.class);
    countryNameList.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(countryNameList);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("2");
    srConfigDTO.setConfigName("2");
    List<SRConfigDTO> serviceArrayList = Mockito.spy(ArrayList.class);
    serviceArrayList.add(srConfigDTO);
    when(srConfigBusiness.getByConfigGroup(any())).thenReturn(serviceArrayList);

    SRConfigDTO srConfigDTOGroup = Mockito.spy(SRConfigDTO.class);
    srConfigDTOGroup.setConfigCode("2");
    srConfigDTOGroup.setConfigName("2");
    List<SRConfigDTO> serviceGroup = Mockito.spy(ArrayList.class);
    serviceGroup.add(srConfigDTOGroup);
    when(srConfigBusiness.getListConfigGroup(any())).thenReturn(serviceGroup);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(2L);
    unitDTO.setUnitCode("2NS");
    unitDTO.setUnitName("2KO");
    List<UnitDTO> unitNameList = Mockito.spy(ArrayList.class);
    unitNameList.add(unitDTO);
    when(unitBusiness.getListUnit(null)).thenReturn(unitNameList);

    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    srRoleDTO.setRoleCode("2X3");
    srRoleDTO.setRoleName("2l3");
    List<SRRoleDTO> srRoleDTOList = Mockito.spy(ArrayList.class);
    srRoleDTOList.add(srRoleDTO);
    when(srRoleBusiness.getListSRRoleByLocationCBB(any())).thenReturn(srRoleDTOList);

    SRFlowExecuteDTO srFlowExecuteDTO = Mockito.spy(SRFlowExecuteDTO.class);
    srFlowExecuteDTO.setFlowId(2L);
    srFlowExecuteDTO.setFlowName("2");
    List<SRFlowExecuteDTO> srFlowExecuteDTOList = Mockito.spy(ArrayList.class);
    srFlowExecuteDTOList.add(srFlowExecuteDTO);
    when(srFlowExecuteBusiness.getListSRFlowExecuteCBB(any())).thenReturn(srFlowExecuteDTOList);

    srCatalogBusiness.getCatalogTemplate();
  }

  @Test
  public void test_getChildTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("srCatalogChild.title")).thenReturn("Dịch vụ cha con");
    PowerMockito.when(I18n.getLanguage("srCatalogChild.listService"))
        .thenReturn("Danh sách dịch vụ");
    srCatalogBusiness.getChildTemplate();
  }

  @Test
  public void testImportDataCatalogChild_01() throws Exception {
    ResultInSideDto actual = srCatalogBusiness
        .importDataCatalogChild(null);

    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void testImportDataCatalogChild_02() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp.xlsx";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    ResultInSideDto actual = srCatalogBusiness
        .importDataCatalogChild(multipartFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void testImportDataCatalogChild_03() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp.xlsx";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{"1", "1*", "1*", "1*"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 4, 0, 3, 1000)
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{"1", "1", "1", "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 1510; i++) {
      dataImportList.add(objects1);
    }
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 5, 0, 3, 1000)
    ).thenReturn(dataImportList);

    ResultInSideDto actual = srCatalogBusiness
        .importDataCatalogChild(multipartFile);

    Assert.assertEquals(RESULT.DATA_OVER, actual.getKey());
  }

  @Test
  public void testImportDataCatalogChild_04() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp.xlsx";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{"1", "1*", "1*", "1*"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 4, 0, 3, 1000)
    ).thenReturn(headerList);

    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 5, 0, 19, 1000)
    ).thenReturn(dataImportList);

    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), any())
    ).thenReturn(fileImp);

    ResultInSideDto actual = srCatalogBusiness
        .importDataCatalogChild(multipartFile);

    Assert.assertEquals(RESULT.NODATA, actual.getKey());
  }

  @Test
  public void testImportDataCatalogChild_05() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp.xlsx";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{"1", "1*", "1*", "1*"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 4, 0, 3, 1000)
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{"1", "1", "1", "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(objects1);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 5, 0, 3, 1000)
    ).thenReturn(dataImportList);

    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), any())
    ).thenReturn(fileImp);

    ResultInSideDto actual = srCatalogBusiness
        .importDataCatalogChild(multipartFile);

    Assert.assertEquals(RESULT.ERROR, actual.getKey());
  }

  @Test
  public void testImportDataCatalogChild_06() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("srCatalogChild.action.1")).thenReturn("1");
    PowerMockito.when(I18n.getLanguage("srCatalogChild.action.0")).thenReturn("0");
    PowerMockito.when(I18n.getLanguage("srCatalogChild.err.noDataServiceCodeChild"))
        .thenReturn(null);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp.xlsx";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{"1", "1*", "1*", "1*"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 4, 0, 3, 1000)
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{"1", "1", "1", "1"};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(objects1);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 5, 0, 3, 1000)
    ).thenReturn(dataImportList);

    List<SRCatalogChildDTO> srCatalogChildDTOList = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        srCatalogChildRepository
            .getListCatalogChild(any())
    ).thenReturn(srCatalogChildDTOList);

    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setServiceId(1L);
    List<SRCatalogDTO> dtoList = Mockito.spy(ArrayList.class);
    dtoList.add(srCatalogDTO);
    PowerMockito.when(
        srCatalogRepository.getListSRCatalog(any())
    ).thenReturn(dtoList);

    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), any())
    ).thenReturn(fileImp);

    ResultInSideDto actual = srCatalogBusiness
        .importDataCatalogChild(multipartFile);

    Assert.assertNull(actual);
  }

  @Test
  public void testImportDataCatalog_01() throws Exception {
    ResultInSideDto actual = srCatalogBusiness
        .importDataCatalog(null);

    Assert.assertEquals(RESULT.FILE_IS_NULL, actual.getKey());
  }

  @Test
  public void testImportDataCatalog_02() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp.xlsx";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    ResultInSideDto actual = srCatalogBusiness
        .importDataCatalog(multipartFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void testImportDataCatalog_03() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp.xlsx";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{
        "1", "1*", "1*", "1*", "1*", "1*", "1*", "1*",
        "1*", "1", "1", "1*", "1*", "1*", "1", "1",
        "1", "1", "1", "1", "1", "1"
    };
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 4, 0, 22, 1000)
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{
        "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1", "1"
    };
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i <= 1510; i++) {
      dataImportList.add(objects1);
    }
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 5, 0, 22, 1000)
    ).thenReturn(dataImportList);

    ResultInSideDto actual = srCatalogBusiness
        .importDataCatalog(multipartFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void testImportDataCatalog_04() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp.xlsx";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{
        "1", "1*", "1*", "1*", "1*", "1*", "1*", "1*",
        "1*", "1", "1", "1*", "1*", "1*", "1", "1",
        "1", "1", "1", "1", "1", "1"
    };
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 4, 0, 22, 1000)
    ).thenReturn(headerList);

    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 5, 0, 22, 1000)
    ).thenReturn(dataImportList);

    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), any())
    ).thenReturn(fileImp);

    ResultInSideDto actual = srCatalogBusiness
        .importDataCatalog(multipartFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void testImportDataCatalog_05() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLocale()).thenReturn("vi_VN");
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");

    MockMultipartFile multipartFile = new MockMultipartFile(
        "data", "filename",
        "text/plain",
        "some xml".getBytes()
    );
    String filePath = "/temp.xlsx";
    PowerMockito.when(
        FileUtils.saveTempFile(anyString(), any(), any())
    ).thenReturn(filePath);
    File fileImp = new File(filePath);

    Object[] objects = new Object[]{
        "1", "1*", "1*", "1*", "1*", "1*", "1*", "1*",
        "1*", "1", "1", "1*", "1*", "1*", "1", "1",
        "1", "1", "1", "1", "1", "1"
    };
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objects);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 4, 0, 22, 1000)
    ).thenReturn(headerList);

    Object[] objects1 = new Object[]{
        "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "1", "1", "1", "1", "1", "1", "1",
        "1", "19/05/2020", "1", "1", "1", "1"
    };
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    dataImportList.add(objects1);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(fileImp, 0, 5, 0, 22, 1000)
    ).thenReturn(dataImportList);

    SRRoleDTO srRoleDTO = Mockito.spy(SRRoleDTO.class);
    srRoleDTO.setCountry("1");
    srRoleDTO.setRoleCode("1");
    List<SRRoleDTO> srRoleDTOList = Mockito.spy(ArrayList.class);
    srRoleDTOList.add(srRoleDTO);
    PowerMockito.when(
        srRoleBusiness.getListSRRoleByLocationCBB(any())
    ).thenReturn(srRoleDTOList);

    UnitDTO unitDTO = Mockito.spy(UnitDTO.class);
    unitDTO.setUnitId(1L);
    List<UnitDTO> unitDTOS = Mockito.spy(ArrayList.class);
    unitDTOS.add(unitDTO);
    PowerMockito.when(
        unitBusiness.getListUnit(any())
    ).thenReturn(unitDTOS);

    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setDisplayStr("1");
    itemDataCRInside.setValueStr(1L);
    List<ItemDataCRInside> lstCountryName = Mockito.spy(ArrayList.class);
    lstCountryName.add(itemDataCRInside);
    PowerMockito.when(
        catLocationBusiness
            .getListLocationByLevelCBB(any(), anyLong(), any())
    ).thenReturn(lstCountryName);

    SRConfigDTO srConfigDTO = Mockito.spy(SRConfigDTO.class);
    srConfigDTO.setConfigCode("1");
    srConfigDTO.setConfigName("1");
    List<SRConfigDTO> systemCallOut = Mockito.spy(ArrayList.class);
    systemCallOut.add(srConfigDTO);
    PowerMockito.when(
        srConfigBusiness.getByConfigGroup(any())
    ).thenReturn(systemCallOut);

    PowerMockito.when(
        CommonExport.exportExcel(anyString(), anyString(), anyList(), anyString(), any())
    ).thenReturn(fileImp);

    ResultInSideDto actual = srCatalogBusiness
        .importDataCatalog(multipartFile);

    Assert.assertEquals(RESULT.FILE_INVALID_FORMAT, actual.getKey());
  }

  @Test
  public void getListServiceNameToMapping() {

  }

  @Test
  public void getListSRCatalogDTO() {
  }

  @Test
  public void test_exportData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCr("1");
    srCatalogDTO.setWo("1");
    srCatalogDTO.setIsAddDay(1L);
    srCatalogDTO.setStatus("A");
    List<SRCatalogDTO> srCatalogDTOList = Mockito.spy(ArrayList.class);
    srCatalogDTOList.add(srCatalogDTO);
    PowerMockito.when(srCatalogRepository.getListDataExport(any())).thenReturn(srCatalogDTOList);
    File result = srCatalogBusiness.exportData(srCatalogDTO);
    assertNull(result);
  }

  @Test
  public void test_exportData_1() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("a");
    SRCatalogDTO srCatalogDTO = Mockito.spy(SRCatalogDTO.class);
    srCatalogDTO.setCr("0");
    srCatalogDTO.setWo("0");
    srCatalogDTO.setIsAddDay(0L);
    srCatalogDTO.setStatus("I");
    List<SRCatalogDTO> srCatalogDTOList = Mockito.spy(ArrayList.class);
    srCatalogDTOList.add(srCatalogDTO);
    PowerMockito.when(srCatalogRepository.getListDataExport(any())).thenReturn(srCatalogDTOList);
    File result = srCatalogBusiness.exportData(srCatalogDTO);
    assertNull(result);
  }

  @Test
  public void test_importDataUnit() throws Exception {
    SRRoleUserInSideDTO srRoleUserDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    ResultInSideDto result = srCatalogBusiness.importDataUnit(null, srRoleUserDTO);
    assertEquals(result.getKey(), "FILE_IS_NULL");
  }

  @Test
  public void test_importDataUnit_1() throws Exception {
    SRRoleUserInSideDTO srRoleUserDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    PowerMockito.mockStatic(CommonImport.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    ResultInSideDto result = srCatalogBusiness.importDataUnit(firstFile, srRoleUserDTO);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataUnit_2() throws Exception {
    SRRoleUserInSideDTO srRoleUserDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserDTO.setUnitId(1L);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    Object[] header = new Object[]{"1", "1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        4,
        0,
        1,
        1000)).thenReturn(headerList);
    ResultInSideDto result = srCatalogBusiness.importDataUnit(firstFile, srRoleUserDTO);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataUnit_3() throws Exception {
    SRRoleUserInSideDTO srRoleUserDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserDTO.setUnitId(1L);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    Object[] header = new Object[]{"1", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        4,
        0,
        1,
        1000)).thenReturn(headerList);
    ResultInSideDto result = srCatalogBusiness.importDataUnit(firstFile, srRoleUserDTO);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataUnit_4() throws Exception {
    SRRoleUserInSideDTO srRoleUserDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserDTO.setUnitId(1L);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    Object[] header = new Object[]{"a", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        4,
        0,
        1,
        1000)).thenReturn(headerList);
    ResultInSideDto result = srCatalogBusiness.importDataUnit(firstFile, srRoleUserDTO);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataUnit_5() throws Exception {
    SRRoleUserInSideDTO srRoleUserDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserDTO.setUnitId(1L);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    Object[] header1 = new Object[]{"a", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header1);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        4,
        0,
        1,
        1000)).thenReturn(headerList);
    ResultInSideDto result = srCatalogBusiness.importDataUnit(firstFile, srRoleUserDTO);
    assertEquals(result.getKey(), "FILE_INVALID_FORMAT");
  }

  @Test
  public void test_importDataUnit_6() throws Exception {
    SRRoleUserInSideDTO srRoleUserDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserDTO.setUnitId(1L);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    Object[] header = new Object[]{"a", "a*"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        4,
        0,
        1,
        1000)).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        5,
        0,
        1,
        1000)).thenReturn(headerList1);
    ResultInSideDto result = srCatalogBusiness.importDataUnit(firstFile, srRoleUserDTO);
    assertEquals(result.getKey(), "NODATA");
  }

  @Test
  public void test_importDataUnit_7() throws Exception {
    SRRoleUserInSideDTO srRoleUserDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserDTO.setUnitId(1L);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("a");
    Object[] header = new Object[]{"a", "a*"};
    Object[] header1 = new Object[]{"a", "a*"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1600; i++) {
      headerList1.add(header1);
    }
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        4,
        0,
        1,
        1000)).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        5,
        0,
        1,
        1000)).thenReturn(headerList1);
    ResultInSideDto result = srCatalogBusiness.importDataUnit(firstFile, srRoleUserDTO);
    assertEquals(result.getKey(), "DATA_OVER");
  }

  @Test
  public void test_importDataUnit_8() throws Exception {
    SRRoleUserInSideDTO srRoleUserDTO = Mockito.spy(SRRoleUserInSideDTO.class);
    srRoleUserDTO.setUnitId(1L);
    srRoleUserDTO.setCountry("1");
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(I18n.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("1");
    Object[] header = new Object[]{"1", "1*"};
    Object[] header1 = new Object[]{"1", "1"};
    Object[] header2 = new Object[]{"", "1"};
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(header);
    List<Object[]> headerList1 = Mockito.spy(ArrayList.class);
    headerList1.add(header1);
    headerList1.add(header2);
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        4,
        0,
        1,
        1000)).thenReturn(headerList);
    PowerMockito.when(CommonImport.getDataFromExcelFile(new File(filePath),
        0,
        5,
        0,
        1,
        1000)).thenReturn(headerList1);
    PowerMockito.when(srRoleUserRepository.checkRoleUserExistByUnitId(srRoleUserDTO))
        .thenReturn(srRoleUserDTO);
    ResultInSideDto result = srCatalogBusiness.importDataUnit(firstFile, srRoleUserDTO);
    assertEquals(result.getKey(), "ERROR");
  }


  static void setFinalStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(null, newValue);
  }
}

