package com.viettel.gnoc.risk.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.risk.dto.RiskTypeDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDetailDTO;
import com.viettel.gnoc.risk.repository.RiskTypeDetailRepository;
import com.viettel.gnoc.risk.repository.RiskTypeRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RiskTypeBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class RiskTypeBusinessImplTest {

  @InjectMocks
  RiskTypeBusinessImpl riskTypeBusiness;

  @Mock
  RiskTypeRepository riskTypeRepository;

  @Mock
  RiskTypeDetailRepository riskTypeDetailRepository;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  TicketProvider ticketProvider;

  @Test
  public void getDataRiskTypeSearchWeb() {
    RiskTypeDTO riskTypeDTO = Mockito.spy(RiskTypeDTO.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    when(riskTypeRepository.getDataRiskTypeSearchWeb(any())).thenReturn(datatable);
    Datatable result = riskTypeBusiness.getDataRiskTypeSearchWeb(riskTypeDTO);
    assertEquals(datatable.getPages(), result.getPages());
  }

  @Test
  public void getListRiskTypeDTO() {
    RiskTypeDTO riskTypeDTO = Mockito.spy(RiskTypeDTO.class);
    List<RiskTypeDTO> lst = Mockito.spy(ArrayList.class);
    when(riskTypeRepository.getListRiskTypeDTO(any())).thenReturn(lst);
    List<RiskTypeDTO> result = riskTypeBusiness.getListRiskTypeDTO(riskTypeDTO);
    assertEquals(lst.size(), result.size());
  }

  @Test
  public void getListRiskTypeDetail() {
    RiskTypeDetailDTO riskTypeDetailDTO = Mockito.spy(RiskTypeDetailDTO.class);
    List<RiskTypeDetailDTO> lst = Mockito.spy(ArrayList.class);
    when(riskTypeDetailRepository.getListRiskTypeDetail(any())).thenReturn(lst);
    List<RiskTypeDetailDTO> result = riskTypeBusiness.getListRiskTypeDetail(riskTypeDetailDTO);
    assertEquals(lst.size(), result.size());

  }

  @Test
  public void findRiskTypeByIdFromWeb() {
    Long riskTypeId = 2L;
    RiskTypeDTO riskTypeDTO = Mockito.spy(RiskTypeDTO.class);
    when(riskTypeRepository.findRiskTypeByIdFromWeb(any())).thenReturn(riskTypeDTO);
    List<RiskTypeDetailDTO> listRiskTypeDetailDTO = Mockito.spy(ArrayList.class);
    when(riskTypeDetailRepository.getListRiskTypeDetail(any())).thenReturn(listRiskTypeDetailDTO);
    RiskTypeDTO result = riskTypeBusiness.findRiskTypeByIdFromWeb(riskTypeId);
    assertNotNull(result);
  }

  @Test
  public void insertOrUpdateRiskType() {
    RiskTypeDetailDTO riskTypeDetailDTO = Mockito.spy(RiskTypeDetailDTO.class);
    List<RiskTypeDetailDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(riskTypeDetailDTO);

    RiskTypeDTO riskTypeDTO = Mockito.spy(RiskTypeDTO.class);
    riskTypeDTO.setRiskTypeId(2L);
    riskTypeDTO.setListRiskTypeDetailDTO(lst);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    when(riskTypeRepository.insertOrUpdateRiskType(any())).thenReturn(resultInSideDto);

    ResultInSideDto resultInSideDtoAdd = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoAdd.setKey(RESULT.SUCCESS);
    when(riskTypeDetailRepository.add(any())).thenReturn(resultInSideDtoAdd);
    when(riskTypeRepository.findRiskTypeByIdFromWeb(anyLong())).thenReturn(new RiskTypeDTO());

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    riskTypeBusiness.insertOrUpdateRiskType(riskTypeDTO);
  }

  @Test
  public void insertOrUpdateRiskType1() {
    RiskTypeDetailDTO riskTypeDetailDTO = Mockito.spy(RiskTypeDetailDTO.class);
    List<RiskTypeDetailDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(riskTypeDetailDTO);

    RiskTypeDTO riskTypeDTO = Mockito.spy(RiskTypeDTO.class);
    riskTypeDTO.setRiskTypeId(null);
    riskTypeDTO.setListRiskTypeDetailDTO(lst);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    when(riskTypeRepository.insertOrUpdateRiskType(any())).thenReturn(resultInSideDto);

    ResultInSideDto resultInSideDtoAdd = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoAdd.setKey(RESULT.SUCCESS);
    when(riskTypeDetailRepository.add(any())).thenReturn(resultInSideDtoAdd);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    riskTypeBusiness.insertOrUpdateRiskType(riskTypeDTO);
  }

  @Test
  public void delete() {
    Long riskTypeId = 2L;
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    when(riskTypeRepository.delete(any())).thenReturn(resultInSideDto);
    when(riskTypeRepository.findRiskTypeByIdFromWeb(anyLong())).thenReturn(new RiskTypeDTO());
    when(riskTypeRepository.delete(anyLong())).thenReturn(resultInSideDto);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    ResultInSideDto result = riskTypeBusiness.delete(riskTypeId);
    assertEquals(resultInSideDto, result);
  }

  @Test
  public void exportDataRiskType() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    RiskTypeDTO riskTypeDTO = Mockito.spy(RiskTypeDTO.class);
    RiskTypeDTO riskTypeDTOtest = Mockito.spy(RiskTypeDTO.class);
    riskTypeDTOtest.setRiskTypeId(1L);
    List<RiskTypeDTO> list = Mockito.spy(ArrayList.class);
    list.add(riskTypeDTOtest);
    when(riskTypeRepository.getListRiskTypeExport(any())).thenReturn(list);
//    RiskTypeDetailDTO riskTypeDTO1 = Mockito.spy(RiskTypeDetailDTO.class);
    RiskTypeDetailDTO riskTypeDTO1 = new RiskTypeDetailDTO();
    riskTypeDTO1.setRiskTypeId(1L);
    List<RiskTypeDetailDTO> listDetail = Mockito.spy(ArrayList.class);
    listDetail.add(riskTypeDTO1);
    List<RiskTypeDetailDTO> listDetailNull = null;
    PowerMockito.when(riskTypeDetailRepository.getListRiskTypeDetail(riskTypeDTO1))
        .thenReturn(listDetailNull);
    PowerMockito.when(catItemRepository.getListItemByCategory(CATEGORY.RISK_PRIORITY, null))
        .thenReturn(new ArrayList<>());
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    riskTypeBusiness.exportDataRiskType(riskTypeDTO);
  }

  @Test
  public void getTemplateImport() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage("riskType.riskTypeCode")).thenReturn("Mã loại rủi ro");
    PowerMockito.when(I18n.getLanguage("riskType.riskTypeName")).thenReturn("Tên loại rủi ro");
    PowerMockito.when(I18n.getLanguage("riskType.statusName")).thenReturn("Trạng thái");
    PowerMockito.when(I18n.getLanguage("riskType.priorityName")).thenReturn("Mức độ ưu tiên");
    PowerMockito.when(I18n.getLanguage("riskType.processTimeStr")).thenReturn("Thời gian xử lý(H)");
    PowerMockito.when(I18n.getLanguage("riskType.actionName")).thenReturn("Thao tác");

    PowerMockito.when(I18n.getLanguage("riskType.STT")).thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("riskType.riskTypeCode")).thenReturn("Mã loại rủi ro");
    PowerMockito.when(I18n.getLanguage("riskType.riskTypeName")).thenReturn("Tên loại rủi ro");
    PowerMockito.when(I18n.getLanguage("riskType.riskGroupTypeName"))
        .thenReturn("Nhóm loại rủi ro");
    PowerMockito.when(I18n.getLanguage("riskType.statusName")).thenReturn("Trạng thái");
    PowerMockito.when(I18n.getLanguage("riskType.priorityName")).thenReturn("Mức độ ưu tiên");
    PowerMockito.when(I18n.getLanguage("riskType.processTimeStr")).thenReturn("Thời gian xử lý(H)");
    PowerMockito.when(I18n.getLanguage("riskType.warningScheduleStr"))
        .thenReturn("Chu kì nhắn tin cảnh báo quá hạn (D)");
    PowerMockito.when(I18n.getLanguage("riskType.timeWarningOverdueStr"))
        .thenReturn("Thời gian cảnh báo quá hạn (D)");
    PowerMockito.when(I18n.getLanguage("riskType.actionName")).thenReturn("Thao tác");

    PowerMockito.when(I18n.getLanguage("riskType.import.title"))
        .thenReturn("Quản lí thông tin loại rủi ro");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> listCat = Mockito.spy(ArrayList.class);
    listCat.add(catItemDTO);
    Datatable dataRiskGroupType = Mockito.spy(Datatable.class);
    dataRiskGroupType.setData(listCat);
    when(catItemRepository
        .getItemMaster(anyString(), anyString(), anyString(), anyString(), anyString()))
        .thenReturn(dataRiskGroupType);
    riskTypeBusiness.getTemplateImport();
  }

  @Test
  public void importDataRiskType() {
    MultipartFile multipartFile = null;
    riskTypeBusiness.importDataRiskType(multipartFile);
  }

  @Test
  public void importDataRiskType0() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    MockMultipartFile firstFile = new MockMultipartFile("data", "filename.xlsx", "text/plain",
        "some xml".getBytes());
    String filePath = "/temp";
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.scheduleId"))
        .thenReturn("MrScheduleTel.scheduleId");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.nationName"))
        .thenReturn("MrScheduleTel.nationName");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.region"))
        .thenReturn("MrScheduleTel.region");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.arrayCode"))
        .thenReturn("MrScheduleTel.arrayCode");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.networkType"))
        .thenReturn("MrScheduleTel.networkType");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.deviceType"))
        .thenReturn("MrScheduleTel.deviceType");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.deviceCode"))
        .thenReturn("MrScheduleTel.deviceCode");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.deviceName"))
        .thenReturn("MrScheduleTel.deviceName");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.nodeIp"))
        .thenReturn("MrScheduleTel.nodeIp");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.vendor"))
        .thenReturn("MrScheduleTel.vendor");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.mrId"))
        .thenReturn("MrScheduleTel.mrId");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.woCode"))
        .thenReturn("MrScheduleTel.woCode");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.coordinatingUnitHardName"))
        .thenReturn("MrScheduleTel.coordinatingUnitHardName");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.cycle"))
        .thenReturn("MrScheduleTel.cycle");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.cycleTypeName"))
        .thenReturn("MrScheduleTel.cycleTypeName");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.woContent"))
        .thenReturn("MrScheduleTel.woContent");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.lastDateStr"))
        .thenReturn("MrScheduleTel.lastDateStr");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.nextDateStr"))
        .thenReturn("MrScheduleTel.nextDateStr");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.nextDateModifyStr"))
        .thenReturn("MrScheduleTel.nextDateModifyStr");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.updatedDateStr"))
        .thenReturn("MrScheduleTel.updatedDateStr");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.scheduleType"))
        .thenReturn("MrScheduleTel.scheduleType");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.station"))
        .thenReturn("MrScheduleTel.station");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.userMrHard"))
        .thenReturn("MrScheduleTel.userMrHard");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.mrConfirmName"))
        .thenReturn("MrScheduleTel.mrConfirmName");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.mrComment"))
        .thenReturn("MrScheduleTel.mrComment");
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    riskTypeBusiness.importDataRiskType(firstFile);
  }

  @Test
  public void handleFileExport() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    RiskTypeDTO riskTypeDTO = Mockito.spy(RiskTypeDTO.class);
    List<RiskTypeDTO> list = Mockito.spy(ArrayList.class);
    list.add(riskTypeDTO);
    String[] columnExport = {""};
    String date = "";
    String code = "";
    PowerMockito.mockStatic(CommonExport.class);
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    riskTypeBusiness.handleFileExport(list, columnExport, date, code);
  }

  @Test
  public void setMapPriority() {
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    list.add(catItemDTO);
    when(catItemRepository.getListItemByCategory(CATEGORY.RISK_PRIORITY, null)).thenReturn(list);
    riskTypeBusiness.setMapPriority();
  }

  @Test
  public void setMapGroupType() {
    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataGroupType = Mockito.spy(Datatable.class);
    dataGroupType.setData(list);
    when(catItemRepository.getItemMaster(any(), any(), any(), any(), any()))
        .thenReturn(dataGroupType);
    riskTypeBusiness.setMapGroupType();
  }

  @Test
  public void validateImportInfo() {
    PowerMockito.spy(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    Map<String, String> mapImportDTO = new HashMap<>();
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("aaaa");
    riskTypeBusiness.validateImportInfo(importDTO, mapImportDTO);
  }

  @Test
  public void validateImportInfo1() {
    PowerMockito.spy(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("aaaa");
    importDTO.setRiskTypeCode("2");
    importDTO.setRiskTypeName("2");
    importDTO.setStatusName("2");
    importDTO.setPriorityName("2");
    importDTO.setProcessTimeStr("2");
    importDTO.setActionName("2");
    importDTO.setRiskTypeCode("$$$");
    Map<String, String> mapImportDTO = new HashMap<>();
    riskTypeBusiness.validateImportInfo(importDTO, mapImportDTO);
  }

  @Test
  public void validateImportInfo2() {
    PowerMockito.spy(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("aaaa");
    importDTO.setRiskTypeCode("2");
    importDTO.setRiskTypeName("2");
    importDTO.setStatusName("2");
    importDTO.setPriorityName("2");
    importDTO.setProcessTimeStr("2");
    importDTO.setActionName("2");
    importDTO.setRiskTypeCode("_ddd");
    Map<String, String> mapImportDTO = new HashMap<>();
    riskTypeBusiness.validateImportInfo(importDTO, mapImportDTO);
  }

  @Test
  public void validateImportInfo3() {
    PowerMockito.spy(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("aaaa");
    importDTO.setRiskTypeCode("2");
    importDTO.setRiskTypeName("2");
    importDTO.setStatusName("2");
    importDTO.setPriorityName("22");
    importDTO.setProcessTimeStr("2");
    importDTO.setActionName("2");
    importDTO.setRiskTypeCode("ddd__");
    Map<String, String> mapImportDTO = new HashMap<>();
    riskTypeBusiness.validateImportInfo(importDTO, mapImportDTO);
  }

  @Test
  public void validateImportInfo4() {
    PowerMockito.spy(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("aaaa");
    importDTO.setRiskTypeCode("2");
    importDTO.setRiskTypeName("2");
    importDTO.setStatusName("2");
    importDTO.setPriorityName("2");
    importDTO.setProcessTimeStr("2");
    importDTO.setActionName("22");
    String a = "";
    for (int i = 0; i < 501; i++) {
      a += "t";
    }
    importDTO.setRiskTypeCode(a);
    Map<String, String> mapImportDTO = new HashMap<>();
    riskTypeBusiness.validateImportInfo(importDTO, mapImportDTO);
  }

  @Test
  public void validateImportInfo5() {
    PowerMockito.spy(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("aaaa");
    importDTO.setRiskTypeCode("2");
    String a = "";
    for (int i = 0; i < 502; i++) {
      a += "t";
    }
    importDTO.setRiskTypeName(a);
    importDTO.setStatusName("2");
    importDTO.setPriorityName("22");
    importDTO.setProcessTimeStr("2");
    importDTO.setActionName("2");
    importDTO.setRiskTypeCode("22");
    Map<String, String> mapImportDTO = new HashMap<>();
    riskTypeBusiness.validateImportInfo(importDTO, mapImportDTO);
  }

  @Test
  public void validateImportInfo6() {
    PowerMockito.spy(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("aaaa");
    importDTO.setRiskTypeCode("2");
    importDTO.setRiskTypeName("2");
    importDTO.setStatusName("Active");
    importDTO.setPriorityName("22");
    importDTO.setProcessTimeStr("2");
    importDTO.setActionName("2");
    importDTO.setRiskTypeCode("22");
    importDTO.setRiskGroupTypeName("2");
    importDTO.setRiskGroupTypeId(null);
    Map<String, String> mapImportDTO = new HashMap<>();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(2L);
    catItemDTO.setItemName("2");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataGroupType = Mockito.spy(Datatable.class);
    dataGroupType.setData(list);
    when(catItemRepository.getItemMaster(any(), any(), any(), any(), any()))
        .thenReturn(dataGroupType);

    riskTypeBusiness.validateImportInfo(importDTO, mapImportDTO);
  }

  @Test
  public void validateImportInfo7() {
    PowerMockito.spy(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("aaaa");
    PowerMockito.when(I18n.getLanguage("riskType.status.1")).thenReturn("Active");
    importDTO.setRiskTypeCode("2");
    importDTO.setRiskTypeName("2");
    importDTO.setStatusName("Active");
    importDTO.setPriorityName("2");
    importDTO.setProcessTimeStr("aa");
    importDTO.setActionName("22");
    importDTO.setRiskTypeCode("22");
    importDTO.setRiskGroupTypeName("2");
    importDTO.setRiskGroupTypeId(null);
    Map<String, String> mapImportDTO = new HashMap<>();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(2L);
    catItemDTO.setItemName("2");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataGroupType = Mockito.spy(Datatable.class);
    dataGroupType.setData(list);
    when(catItemRepository.getItemMaster(any(), any(), any(), any(), any()))
        .thenReturn(dataGroupType);

    List<CatItemDTO> listPri = Mockito.spy(ArrayList.class);
    listPri.add(catItemDTO);
    when(catItemRepository.getListItemByCategory(CATEGORY.RISK_PRIORITY, null)).thenReturn(listPri);

    riskTypeBusiness.validateImportInfo(importDTO, mapImportDTO);
  }

  @Test
  public void validateImportInfo8() {
    PowerMockito.spy(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("aaaaaa");
    PowerMockito.when(I18n.getLanguage("riskType.status.1")).thenReturn("Active");
    importDTO.setRiskTypeCode("2");
    importDTO.setRiskTypeName("2");
    importDTO.setStatusName("Active");
    importDTO.setPriorityName("2");
    importDTO.setProcessTimeStr("2.0");
    importDTO.setActionName("22");
    importDTO.setRiskTypeCode("22");
    importDTO.setRiskGroupTypeName("2");
    importDTO.setRiskGroupTypeId(null);
    importDTO.setWarningScheduleStr("1");
    Map<String, String> mapImportDTO = new HashMap<>();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(2L);
    catItemDTO.setItemName("2");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataGroupType = Mockito.spy(Datatable.class);
    dataGroupType.setData(list);
    when(catItemRepository.getItemMaster(any(), any(), any(), any(), any()))
        .thenReturn(dataGroupType);

    List<CatItemDTO> listPri = Mockito.spy(ArrayList.class);
    listPri.add(catItemDTO);
    when(catItemRepository.getListItemByCategory(CATEGORY.RISK_PRIORITY, null)).thenReturn(listPri);

    riskTypeBusiness.validateImportInfo(importDTO, mapImportDTO);
  }

  @Test
  public void validateImportInfo8_1() {
    PowerMockito.spy(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("aaaa");
    PowerMockito.when(I18n.getLanguage("riskType.status.1")).thenReturn("Active");
    importDTO.setRiskTypeCode("2");
    importDTO.setRiskTypeName("2");
    importDTO.setStatusName("Active");
    importDTO.setPriorityName("2");
    String a = "";
    for (int i = 0; i < 11; i++) {
      a += "1";
    }
    importDTO.setProcessTimeStr(a);
    importDTO.setActionName("22");
    importDTO.setRiskTypeCode("22");
    importDTO.setRiskGroupTypeName("2");
    importDTO.setRiskGroupTypeId(null);
    importDTO.setWarningScheduleStr("a");
    Map<String, String> mapImportDTO = new HashMap<>();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(2L);
    catItemDTO.setItemName("2");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataGroupType = Mockito.spy(Datatable.class);
    dataGroupType.setData(list);
    when(catItemRepository.getItemMaster(any(), any(), any(), any(), any()))
        .thenReturn(dataGroupType);

    List<CatItemDTO> listPri = Mockito.spy(ArrayList.class);
    listPri.add(catItemDTO);
    when(catItemRepository.getListItemByCategory(CATEGORY.RISK_PRIORITY, null)).thenReturn(listPri);

    riskTypeBusiness.validateImportInfo(importDTO, mapImportDTO);
  }


  @Test
  public void validateImportInfo9() {
    PowerMockito.spy(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("aaaaaA");
    PowerMockito.when(I18n.getLanguage("riskType.status.1")).thenReturn("Active");
    importDTO.setRiskTypeCode("2");
    importDTO.setRiskTypeName("2");
    importDTO.setStatusName("Active");
    importDTO.setPriorityName("2");
    importDTO.setProcessTimeStr("2.0");
    importDTO.setActionName("22");
    importDTO.setRiskTypeCode("22");
    importDTO.setRiskGroupTypeName("2");
    importDTO.setRiskGroupTypeId(null);
    importDTO.setWarningScheduleStr("-1");
    Map<String, String> mapImportDTO = new HashMap<>();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(2L);
    catItemDTO.setItemName("2");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataGroupType = Mockito.spy(Datatable.class);
    dataGroupType.setData(list);
    when(catItemRepository.getItemMaster(any(), any(), any(), any(), any()))
        .thenReturn(dataGroupType);

    List<CatItemDTO> listPri = Mockito.spy(ArrayList.class);
    listPri.add(catItemDTO);
    when(catItemRepository.getListItemByCategory(CATEGORY.RISK_PRIORITY, null)).thenReturn(listPri);

    riskTypeBusiness.validateImportInfo(importDTO, mapImportDTO);
  }

  @Test
  public void validateImportInfo10() {
    PowerMockito.spy(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("aaaaa");
    PowerMockito.when(I18n.getLanguage("riskType.status.1")).thenReturn("Active");
    importDTO.setRiskTypeCode("2");
    importDTO.setRiskTypeName("2");
    importDTO.setStatusName("Active");
    importDTO.setPriorityName("2");
    importDTO.setProcessTimeStr("2.0");
    importDTO.setActionName("22");
    importDTO.setRiskTypeCode("22");
    importDTO.setRiskGroupTypeName("2");
    importDTO.setRiskGroupTypeId(null);
    importDTO.setWarningScheduleStr("2");
    Map<String, String> mapImportDTO = new HashMap<>();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(2L);
    catItemDTO.setItemName("2");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataGroupType = Mockito.spy(Datatable.class);
    dataGroupType.setData(list);
    when(catItemRepository.getItemMaster(any(), any(), any(), any(), any()))
        .thenReturn(dataGroupType);

    List<CatItemDTO> listPri = Mockito.spy(ArrayList.class);
    listPri.add(catItemDTO);
    when(catItemRepository.getListItemByCategory(CATEGORY.RISK_PRIORITY, null)).thenReturn(listPri);

    riskTypeBusiness.validateImportInfo(importDTO, mapImportDTO);
  }

  @Test
  public void validateImportInfo11() {
    PowerMockito.spy(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("aaaaaaaa");
    PowerMockito.when(I18n.getLanguage("riskType.status.1")).thenReturn("Active");
    PowerMockito.when(I18n.getLanguage("riskType.action.0")).thenReturn("Insert");
    importDTO.setRiskTypeCode("2");
    importDTO.setRiskTypeName("2");
    importDTO.setStatusName("Active");
    importDTO.setPriorityName("2");
    importDTO.setProcessTimeStr("2.0");
    importDTO.setActionName("22");
    importDTO.setRiskTypeCode("22");
    importDTO.setRiskGroupTypeName("2");
    importDTO.setRiskGroupTypeId(null);
    importDTO.setWarningScheduleStr("2");
    Map<String, String> mapImportDTO = new HashMap<>();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(2L);
    catItemDTO.setItemName("2");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataGroupType = Mockito.spy(Datatable.class);
    dataGroupType.setData(list);
    when(catItemRepository.getItemMaster(any(), any(), any(), any(), any()))
        .thenReturn(dataGroupType);

    List<CatItemDTO> listPri = Mockito.spy(ArrayList.class);
    listPri.add(catItemDTO);
    when(catItemRepository.getListItemByCategory(CATEGORY.RISK_PRIORITY, null)).thenReturn(listPri);

    riskTypeBusiness.validateImportInfo(importDTO, mapImportDTO);
  }

  @Test
  public void validateImportInfo12() {
    PowerMockito.spy(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("aaaaaaaa");
    PowerMockito.when(I18n.getLanguage("riskType.status.1")).thenReturn("Active");
    PowerMockito.when(I18n.getLanguage("riskType.action.0")).thenReturn("Insert");
    importDTO.setRiskTypeCode("2");
    importDTO.setRiskTypeName("2");
    importDTO.setStatusName("Active");
    importDTO.setPriorityName("2");
    importDTO.setProcessTimeStr("2.0");
    importDTO.setActionName("Insert");
    importDTO.setRiskTypeCode("22");
    importDTO.setRiskGroupTypeName("2");
    importDTO.setRiskGroupTypeId(null);
    importDTO.setWarningScheduleStr("2");
    Map<String, String> mapImportDTO = new HashMap<>();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(2L);
    catItemDTO.setItemName("2");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataGroupType = Mockito.spy(Datatable.class);
    dataGroupType.setData(list);
    when(catItemRepository.getItemMaster(any(), any(), any(), any(), any()))
        .thenReturn(dataGroupType);

    RiskTypeDTO dtoTmp = Mockito.spy(RiskTypeDTO.class);
    when(riskTypeRepository.checkRiskTypeExit(any())).thenReturn(dtoTmp);

    List<CatItemDTO> listPri = Mockito.spy(ArrayList.class);
    listPri.add(catItemDTO);
    when(catItemRepository.getListItemByCategory(CATEGORY.RISK_PRIORITY, null)).thenReturn(listPri);

    riskTypeBusiness.validateImportInfo(importDTO, mapImportDTO);
  }

  @Test
  public void validateImportInfo13() {
    PowerMockito.spy(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("aaaaaaaaa");
    PowerMockito.when(I18n.getLanguage("riskType.status.1")).thenReturn("Active");
    PowerMockito.when(I18n.getLanguage("riskType.action.1")).thenReturn("Update");
    importDTO.setRiskTypeCode("2");
    importDTO.setRiskTypeName("2");
    importDTO.setStatusName("Active");
    importDTO.setPriorityName("2");
    importDTO.setProcessTimeStr("2.0");
    importDTO.setActionName("Insert");
    importDTO.setRiskTypeCode("22");
    importDTO.setRiskGroupTypeName("2");
    importDTO.setRiskGroupTypeId(null);
    importDTO.setWarningScheduleStr("2");
    Map<String, String> mapImportDTO = new HashMap<>();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(2L);
    catItemDTO.setItemName("2");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataGroupType = Mockito.spy(Datatable.class);
    dataGroupType.setData(list);
    when(catItemRepository.getItemMaster(any(), any(), any(), any(), any()))
        .thenReturn(dataGroupType);

    RiskTypeDTO dtoTmp = Mockito.spy(RiskTypeDTO.class);
    when(riskTypeRepository.checkRiskTypeExit(any())).thenReturn(dtoTmp);

    List<CatItemDTO> listPri = Mockito.spy(ArrayList.class);
    listPri.add(catItemDTO);
    when(catItemRepository.getListItemByCategory(CATEGORY.RISK_PRIORITY, null)).thenReturn(listPri);

    riskTypeBusiness.validateImportInfo(importDTO, mapImportDTO);
  }

  @Test
  public void validateImportInfo15() {
    PowerMockito.spy(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("aaaaaaaa");
    PowerMockito.when(I18n.getLanguage("riskType.status.1")).thenReturn("Active");
    PowerMockito.when(I18n.getLanguage("riskType.action.1")).thenReturn("test");
    importDTO.setRiskTypeCode("2");
    importDTO.setRiskTypeName("2");
    importDTO.setStatusName("Active");
    importDTO.setPriorityName("2");
    importDTO.setProcessTimeStr("2.0");
    importDTO.setActionName("Insert");
    importDTO.setRiskTypeCode("22");
    importDTO.setRiskGroupTypeName("2");
    importDTO.setRiskGroupTypeId(null);
    importDTO.setWarningScheduleStr("2");
    Map<String, String> mapImportDTO = new HashMap<>();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(2L);
    catItemDTO.setItemName("2");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataGroupType = Mockito.spy(Datatable.class);
    dataGroupType.setData(list);
    when(catItemRepository.getItemMaster(any(), any(), any(), any(), any()))
        .thenReturn(dataGroupType);

    RiskTypeDTO dtoTmp = Mockito.spy(RiskTypeDTO.class);
    when(riskTypeRepository.checkRiskTypeExit(any())).thenReturn(dtoTmp);

    List<CatItemDTO> listPri = Mockito.spy(ArrayList.class);
    listPri.add(catItemDTO);
    when(catItemRepository.getListItemByCategory(CATEGORY.RISK_PRIORITY, null)).thenReturn(listPri);

    riskTypeBusiness.validateImportInfo(importDTO, mapImportDTO);
  }

  @Test
  public void validateImportInfo14() {
    PowerMockito.spy(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any())).thenReturn("aaaaaaaa");
    PowerMockito.when(I18n.getLanguage("riskType.status.1")).thenReturn("Active");
    PowerMockito.when(I18n.getLanguage("riskType.action.1")).thenReturn("Insert");
    importDTO.setRiskTypeCode("2");
    importDTO.setRiskTypeName("2");
    importDTO.setStatusName("Active");
    importDTO.setPriorityName("2");
    importDTO.setProcessTimeStr("2.0");
    importDTO.setActionName("Insert");
    importDTO.setRiskTypeCode("22");
    importDTO.setRiskGroupTypeName("2");
    importDTO.setRiskGroupTypeId(null);
    importDTO.setWarningScheduleStr("2");
    Map<String, String> mapImportDTO = new HashMap<>();

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemId(2L);
    catItemDTO.setItemName("2");
    List<CatItemDTO> list = Mockito.spy(ArrayList.class);
    list.add(catItemDTO);
    Datatable dataGroupType = Mockito.spy(Datatable.class);
    dataGroupType.setData(list);
    when(catItemRepository.getItemMaster(any(), any(), any(), any(), any()))
        .thenReturn(dataGroupType);

    RiskTypeDTO dtoTmp = null;
    when(riskTypeRepository.checkRiskTypeExit(any())).thenReturn(dtoTmp);

    List<CatItemDTO> listPri = Mockito.spy(ArrayList.class);
    listPri.add(catItemDTO);
    when(catItemRepository.getListItemByCategory(CATEGORY.RISK_PRIORITY, null)).thenReturn(listPri);

    riskTypeBusiness.validateImportInfo(importDTO, mapImportDTO);
  }

  @Test
  public void validateDuplicateImport() {
    PowerMockito.mockStatic(I18n.class);
    RiskTypeDTO importDTO = Mockito.spy(RiskTypeDTO.class);
    Map<String, String> mapImportDTO = new HashMap<>();
    riskTypeBusiness.validateDuplicateImport(importDTO, mapImportDTO);
  }

  @Test
  public void insertOrUpdateRiskTypeImport() {
    RiskTypeDTO riskTypeDTO = Mockito.spy(RiskTypeDTO.class);
    riskTypeDTO.setAction(0L);
    riskTypeDTO.setRiskTypeId(null);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(1L);
    when(riskTypeRepository.insertOrUpdateRiskType(any())).thenReturn(resultInSideDto);
    when(riskTypeDetailRepository.add(any())).thenReturn(resultInSideDto);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    riskTypeBusiness.insertOrUpdateRiskTypeImport(riskTypeDTO);
  }

  @Test
  public void insertOrUpdateRiskTypeImport1() {
    RiskTypeDTO riskTypeDTO = Mockito.spy(RiskTypeDTO.class);
    riskTypeDTO.setAction(0L);
    riskTypeDTO.setRiskTypeId(2L);

    riskTypeBusiness.insertOrUpdateRiskTypeImport(riskTypeDTO);
  }

  @Test
  public void insertOrUpdateRiskTypeImport2() {
    RiskTypeDTO riskTypeDTO = Mockito.spy(RiskTypeDTO.class);
    riskTypeDTO.setAction(1L);
    riskTypeDTO.setRiskTypeId(2L);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(2L);
    when(riskTypeRepository.insertOrUpdateRiskType(any())).thenReturn(resultInSideDto);
    when(riskTypeRepository.findRiskTypeByIdFromWeb(anyLong())).thenReturn(new RiskTypeDTO());
    when(riskTypeDetailRepository.add(any())).thenReturn(resultInSideDto);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999L);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    riskTypeBusiness.insertOrUpdateRiskTypeImport(riskTypeDTO);
  }

  @Test
  public void insertOrUpdateRiskTypeImport3() {
    RiskTypeDTO riskTypeDTO = Mockito.spy(RiskTypeDTO.class);
    riskTypeDTO.setAction(1L);
    riskTypeDTO.setRiskTypeId(null);

    riskTypeBusiness.insertOrUpdateRiskTypeImport(riskTypeDTO);
  }

  @Test
  public void getListHeaderSheet() {

  }
}
