package com.viettel.gnoc.mr.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureTelDTO;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.mr.repository.MrCfgProcedureTelRepository;
import com.viettel.gnoc.mr.repository.MrDeviceRepository;
import com.viettel.gnoc.mr.repository.MrRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelHisRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
import com.viettel.gnoc.mr.repository.MrServiceRepository;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
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
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MrScheduleTelBusinessImpl.class, FileUtils.class, CommonImport.class, I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class MrScheduleTelBusinessImplTest {

  @Mock
  MrScheduleTelRepository mrScheduleTelRepository;

  @InjectMocks
  MrScheduleTelBusinessImpl mrScheduleTelBusiness;

  @Mock
  MrCfgProcedureTelRepository mrCfgProcedureTelRepository;

  @Mock
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  MrDeviceRepository mrDeviceRepository;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;

  @Mock
  UserRepository userRepository;

  @Mock
  MrRepository mrRepository;

  @Mock
  MrDeviceBusiness mrDeviceBusiness;


  @Mock
  Map<Long, MrScheduleTelDTO> mapDataImport;

  @Mock
  MrScheduleTelHisRepository mrScheduleTelHisRepository;

  @Mock
  CrServiceProxy crServiceProxy;

  @Mock
  MrServiceRepository mrServiceRepository;

  @Mock
  WoServiceProxy woServiceProxy;

  @Before
  public void setUpUploadFolder() {
    Map<Long, MrScheduleTelDTO> mapDataImport = new HashMap<>();
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mapDataImport.put(1L, mrScheduleTelDTO);
    ReflectionTestUtils.setField(mrScheduleTelBusiness, "mapDataImport",
        mapDataImport);
    ReflectionTestUtils.setField(mrScheduleTelBusiness, "tempFolder",
        "./test_junit");
  }

  @Test
  public void getListMrScheduleTel() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    Datatable datatable = PowerMockito.mock(Datatable.class);

    when(datatable.getTotal()).thenReturn(10);
    when(mrScheduleTelRepository.getListMrScheduleTel(any(), any())).thenReturn(datatable);
    Datatable result = mrScheduleTelBusiness.getListMrScheduleTel(new MrScheduleTelDTO());
    assertEquals(datatable, result);
  }

  @Test
  public void onSearchScheduleTel() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    Datatable datatable = PowerMockito.mock(Datatable.class);
    when(datatable.getTotal()).thenReturn(10);
    when(mrScheduleTelRepository.getListMrScheduleTel(any(), anyString())).thenReturn(datatable);
    Datatable result = mrScheduleTelBusiness.onSearchScheduleTel(new MrScheduleTelDTO());
    assertEquals(datatable, result);
  }

  @Test
  public void getDetail() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(1L);
    when(mrScheduleTelRepository.getDetail(anyLong(), anyString())).thenReturn(mrScheduleTelDTO);
    MrScheduleTelDTO result = mrScheduleTelBusiness
        .getDetail(mrScheduleTelDTO.getScheduleId(), "MR_TYPE_HARD");
    assertEquals(mrScheduleTelDTO, result);
  }

  @Test
  public void insertMrScheduleTelHis() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String type = "S";
    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    MrScheduleTelHisDTO mrScheduleTelHisDTO = Mockito.spy(MrScheduleTelHisDTO.class);
    MrScheduleTelDTO mrScheduleTelDTO1 = Mockito.spy(MrScheduleTelDTO.class);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    MrCfgProcedureTelDTO mrCfgProcedureTelDTO = Mockito.spy(MrCfgProcedureTelDTO.class);
    mrScheduleTelDTO1.setMrId(null);
    mrScheduleTelDTO1.setProcedureId(3L);
    mrScheduleTelDTO1.setMrConfirm("3");
    mrScheduleTelDTO1.setNextDateModify(null);
    when(mrScheduleTelRepository.findMrById(anyLong())).thenReturn(mrInsideDTO);
    when(mrCfgProcedureTelRepository.findMrCfgProcedureTelById(anyLong()))
        .thenReturn(mrCfgProcedureTelDTO);
    mrScheduleTelBusiness
        .insertMrScheduleTelHis(mrScheduleTelHisDTO, mrScheduleTelDTO1, mrScheduleTelDTO, type);
  }

  @Test
  public void insertMrScheduleTelHis1() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    String type = "S";
    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    MrScheduleTelHisDTO mrScheduleTelHisDTO = Mockito.spy(MrScheduleTelHisDTO.class);
    MrScheduleTelDTO mrScheduleTelDTO1 = Mockito.spy(MrScheduleTelDTO.class);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    MrCfgProcedureTelDTO mrCfgProcedureTelDTO = Mockito.spy(MrCfgProcedureTelDTO.class);
    mrScheduleTelDTO1.setMrId(2L);
    mrScheduleTelDTO1.setProcedureId(null);
    mrScheduleTelDTO1.setMrConfirm(null);
    mrScheduleTelDTO1.setNextDateModify(new Date());
    when(mrScheduleTelRepository.findMrById(anyLong())).thenReturn(mrInsideDTO);
    when(mrCfgProcedureTelRepository.findMrCfgProcedureTelById(anyLong()))
        .thenReturn(mrCfgProcedureTelDTO);
    mrScheduleTelBusiness
        .insertMrScheduleTelHis(mrScheduleTelHisDTO, mrScheduleTelDTO1, mrScheduleTelDTO, type);
  }

  @Test
  public void updateMrScheduleTelDTO() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setMrHardCycle(null);
    mrScheduleTelDTO.setMrId(1L);
    mrScheduleTelDTO.setMrConfirm("6");
    mrScheduleTelDTO.setCycle("1");
    ResultInSideDto vadidateOk = Mockito.spy(ResultInSideDto.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    when(resultInSideDto.getKey()).thenReturn(RESULT.ERROR);
    when(vadidateOk.getKey()).thenReturn((RESULT.SUCCESS));
    when(mrScheduleTelRepository.updateMrScheduleTel(any())).thenReturn(resultInSideDto);
    when(mrDeviceRepository.findMrDeviceById(any())).thenReturn(mrDeviceDTO);
    when(mrScheduleTelBusiness.getDetail(any(), any())).thenReturn(mrScheduleTelDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Bản ghi đã có Mr/Wo không được phép cập nhật");
    mrScheduleTelBusiness.updateMrScheduleTelDTO(mrScheduleTelDTO);
  }

  @Test
  public void updateMrScheduleTelDTO1() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    MrScheduleTelDTO mrScheduleTelDTOReturn = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTOReturn.setNextDateModify(
        DateTimeUtils.convertDateToOffset(new Date(), 1D, true));
    mrScheduleTelDTOReturn.setMrHardCycle(null);
    mrScheduleTelDTOReturn.setMrConfirm(null);
    mrScheduleTelDTOReturn.setCycle("1");
    ResultInSideDto vadidateOk = Mockito.spy(ResultInSideDto.class);
    vadidateOk.setKey(RESULT.SUCCESS);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    when(resultInSideDto.getKey()).thenReturn(RESULT.ERROR);
    when(vadidateOk.getKey()).thenReturn((RESULT.SUCCESS));
    when(mrScheduleTelRepository.updateMrScheduleTel(any())).thenReturn(resultInSideDto);
//    when(mrDeviceRepository.updateMrDeviceServices(any())).thenReturn(mrDeviceDTO);
    when(mrDeviceRepository.findMrDeviceById(any())).thenReturn(mrDeviceDTO);
    when(mrScheduleTelBusiness.getDetail(any(), any())).thenReturn(mrScheduleTelDTOReturn);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Cập nhật thành công");
    mrScheduleTelBusiness.updateMrScheduleTelDTO(mrScheduleTelDTOReturn);
  }

  @Test
  public void updateMrScheduleTelDTO2() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    MrScheduleTelDTO mrScheduleTelDTOReturn = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTOReturn.setNextDateModify(
        DateTimeUtils.convertDateToOffset(new Date(), 1D, true));
    mrScheduleTelDTOReturn.setMrHardCycle(null);
    mrScheduleTelDTOReturn.setMrConfirm(null);
    mrScheduleTelDTOReturn.setCycle("1");
    ResultInSideDto vadidateOk = Mockito.spy(ResultInSideDto.class);
    vadidateOk.setKey(RESULT.SUCCESS);
    resultInSideDto.setKey(RESULT.FAIL);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    when(resultInSideDto.getKey()).thenReturn(RESULT.ERROR);
    when(vadidateOk.getKey()).thenReturn((RESULT.SUCCESS));
    when(mrScheduleTelRepository.updateMrScheduleTel(any())).thenReturn(resultInSideDto);
//    when(mrDeviceRepository.updateMrDeviceServices(any())).thenReturn(mrDeviceDTO);
    when(mrDeviceRepository.findMrDeviceById(any())).thenReturn(mrDeviceDTO);
    when(mrScheduleTelBusiness.getDetail(any(), any())).thenReturn(mrScheduleTelDTOReturn);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Cập nhật không thành công");
    mrScheduleTelBusiness.updateMrScheduleTelDTO(mrScheduleTelDTOReturn);
  }


  @Test
  public void updateMrScheduleTelDTO3() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    MrScheduleTelDTO mrScheduleTelDTOReturn = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTOReturn.setCycleType("M");
    mrScheduleTelDTOReturn.setMrHardCycle(null);
    mrScheduleTelDTOReturn.setMrConfirm("6");
    mrScheduleTelDTOReturn.setCycle("1");
    ResultInSideDto vadidateOk = Mockito.spy(ResultInSideDto.class);
    vadidateOk.setKey(RESULT.SUCCESS);
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    when(resultInSideDto.getKey()).thenReturn(RESULT.ERROR);
    when(vadidateOk.getKey()).thenReturn((RESULT.SUCCESS));
    when(mrScheduleTelRepository.updateMrScheduleTel(any())).thenReturn(resultInSideDto);
    when(mrDeviceRepository.updateMrDeviceServices(any())).thenReturn(resultInSideDto);
    when(mrDeviceRepository.findMrDeviceById(any())).thenReturn(mrDeviceDTO);
    when(mrScheduleTelBusiness.getDetail(any(), any())).thenReturn(mrScheduleTelDTOReturn);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Cập nhật thành công");
    mrScheduleTelBusiness.updateMrScheduleTelDTO(mrScheduleTelDTOReturn);
    mrScheduleTelDTOReturn.setCycle("1");
    mrScheduleTelBusiness.updateMrScheduleTelDTO(mrScheduleTelDTOReturn);
    mrScheduleTelDTOReturn.setCycle("3");
    mrScheduleTelBusiness.updateMrScheduleTelDTO(mrScheduleTelDTOReturn);
    mrScheduleTelDTOReturn.setCycle("6");
    mrScheduleTelBusiness.updateMrScheduleTelDTO(mrScheduleTelDTOReturn);
    mrScheduleTelDTOReturn.setCycle("12");
    mrScheduleTelBusiness.updateMrScheduleTelDTO(mrScheduleTelDTOReturn);
  }

  @Test
  public void updateMrScheduleTelDTO4() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    MrScheduleTelDTO mrScheduleTelDTOReturn = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTOReturn.setCycleType("M");
    mrScheduleTelDTOReturn.setMrHardCycle(null);
    mrScheduleTelDTOReturn.setMrConfirm("6");
    mrScheduleTelDTOReturn.setCycle("1");
    ResultInSideDto vadidateOk = Mockito.spy(ResultInSideDto.class);
    vadidateOk.setKey(RESULT.SUCCESS);
    resultInSideDto.setKey(RESULT.ERROR);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    when(resultInSideDto.getKey()).thenReturn(RESULT.ERROR);
    when(vadidateOk.getKey()).thenReturn((RESULT.SUCCESS));
    when(mrScheduleTelRepository.updateMrScheduleTel(any())).thenReturn(resultInSideDto);
    when(mrDeviceRepository.updateMrDeviceServices(any())).thenReturn(resultInSideDto);
    when(mrDeviceRepository.findMrDeviceById(any())).thenReturn(mrDeviceDTO);
    when(mrScheduleTelBusiness.getDetail(any(), any())).thenReturn(mrScheduleTelDTOReturn);
//    when(mrScheduleTelHisRepository.insertScheduleHis(any())).thenReturn(resultInSideDto);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Cập nhật không thành công");
    mrScheduleTelBusiness.updateMrScheduleTelDTO(mrScheduleTelDTOReturn);
    mrScheduleTelDTOReturn.setCycle("1");
    mrScheduleTelBusiness.updateMrScheduleTelDTO(mrScheduleTelDTOReturn);
    mrScheduleTelDTOReturn.setCycle("3");
    mrScheduleTelBusiness.updateMrScheduleTelDTO(mrScheduleTelDTOReturn);
    mrScheduleTelDTOReturn.setCycle("6");
    mrScheduleTelBusiness.updateMrScheduleTelDTO(mrScheduleTelDTOReturn);
    mrScheduleTelDTOReturn.setCycle("12");
    mrScheduleTelBusiness.updateMrScheduleTelDTO(mrScheduleTelDTOReturn);
  }

  @Test
  public void getConfigByGroup() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<MrConfigDTO> list = Mockito.spy(ArrayList.class);
    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigGroup("test");
    list.add(mrConfigDTO);
    PowerMockito.when(mrScheduleTelRepository.getConfigByGroup(any())).thenReturn(list);
    List<MrConfigDTO> result = Mockito.spy(ArrayList.class);
    result = mrScheduleTelBusiness.getConfigByGroup(mrConfigDTO.getConfigGroup());
    assertEquals(list.size(), result.size());
  }

  @Test
  public void deleteMrScheduleTel() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(4L);
    mrScheduleTelDTO.setMrId(null);
    mrScheduleTelDTO.setWoId(null);
    mrScheduleTelDTO.setMrHard("1");
    mrScheduleTelDTO.setCycleType("M");
    when(mrScheduleTelBusiness.getDetail(any(), any())).thenReturn(mrScheduleTelDTO);
    when(mrDeviceRepository.findMrDeviceById(any())).thenReturn(mrDeviceDTO);
    when(mrScheduleTelRepository.deleteMrScheduleTel(any())).thenReturn(resultInSideDto);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Xóa dữ liệu thành công");
    mrScheduleTelDTO.setCycle("1");
    mrScheduleTelBusiness.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());
    mrScheduleTelDTO.setCycle("3");
    mrScheduleTelBusiness.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());
    mrScheduleTelDTO.setCycle("6");
    mrScheduleTelBusiness.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());
    mrScheduleTelDTO.setCycle("12");
    mrScheduleTelBusiness.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());
  }

  @Test
  public void deleteMrScheduleTel1() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FAIL);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(3L);
    mrScheduleTelDTO.setMrId(null);
    mrScheduleTelDTO.setWoId(null);
    mrScheduleTelDTO.setMrHard("1");
    mrScheduleTelDTO.setCycleType("M");
    when(mrScheduleTelBusiness.getDetail(any(), any())).thenReturn(mrScheduleTelDTO);
    when(mrDeviceRepository.findMrDeviceById(any())).thenReturn(mrDeviceDTO);
    when(mrScheduleTelRepository.deleteMrScheduleTel(any())).thenReturn(resultInSideDto);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Xóa dữ liệu không thành công");
    mrScheduleTelDTO.setCycle("1");
    mrScheduleTelBusiness.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());
    mrScheduleTelDTO.setCycle("3");
    mrScheduleTelBusiness.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());
    mrScheduleTelDTO.setCycle("6");
    mrScheduleTelBusiness.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());
    mrScheduleTelDTO.setCycle("12");
    mrScheduleTelBusiness.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());
  }

  @Test
  public void deleteMrScheduleTel3() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FAIL);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(3L);
    mrScheduleTelDTO.setMrId(1L);
    mrScheduleTelDTO.setWoId(2L);
    mrScheduleTelDTO.setMrHard("1");
    mrScheduleTelDTO.setCycleType("M");
    when(mrScheduleTelBusiness.getDetail(any(), any())).thenReturn(mrScheduleTelDTO);
    when(mrDeviceRepository.findMrDeviceById(any())).thenReturn(mrDeviceDTO);
    when(mrScheduleTelRepository.deleteMrScheduleTel(any())).thenReturn(resultInSideDto);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Xóa không thành công bản ghi chọn có chứa MR/WO");
    mrScheduleTelDTO.setCycle("1");
    mrScheduleTelBusiness.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());
    mrScheduleTelDTO.setCycle("3");
    mrScheduleTelBusiness.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());
    mrScheduleTelDTO.setCycle("6");
    mrScheduleTelBusiness.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());
    mrScheduleTelDTO.setCycle("12");
    mrScheduleTelBusiness.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());
  }

  @Test
  public void getTemplate() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    ItemDataCRInside dataCRInside = Mockito.spy(ItemDataCRInside.class);
    lstMarketCode.add(dataCRInside);
    List<MrDeviceDTO> mrDeviceDTOList = Mockito.spy(ArrayList.class);
    when(catLocationBusiness.getListLocationByLevelCBB(any(), any(), any()))
        .thenReturn(lstMarketCode);
    when(mrDeviceRepository.getListRegionByMarketCode(anyString())).thenReturn(mrDeviceDTOList);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any()))
        .thenReturn("STT");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.region"))
        .thenReturn("Region code");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.arrNet"))
        .thenReturn("Network Type-Name Of Device");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    CatItemDTO itemDTO = Mockito.spy(CatItemDTO.class);
    itemDTO.setItemValue("1");
    itemDTO.setItemName("1");
    lstArrayCode.add(itemDTO);
    when(mrCfgProcedureCDBusiness.getMrSubCategory()).thenReturn(lstArrayCode);
    List<MrDeviceDTO> lstANT = Mockito.spy(ArrayList.class);
    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO.setArrayCode("1");
    MrDeviceDTO mrDeviceDTO2 = Mockito.spy(MrDeviceDTO.class);
    mrDeviceDTO2.setArrayCode("2");
    lstANT.add(mrDeviceDTO);
    lstANT.add(mrDeviceDTO2);
    when(mrDeviceRepository.getMrDeviceByA_N_T()).thenReturn(lstANT);

    List<MrConfigDTO> lstMrConfigDTO = Mockito.spy(ArrayList.class);
    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigName("1");
    lstMrConfigDTO.add(mrConfigDTO);
    when(mrScheduleTelRepository.getConfigByGroup(anyString())).thenReturn(lstMrConfigDTO);
    mrScheduleTelBusiness.getTemplate("H");
  }

  @Test
  public void importData() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any()))
        .thenReturn("Chưa chọn file import");
    mrScheduleTelBusiness.importData(null);
  }

  @Test
  public void importData1() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FAIL);
    String filePath = "/temp";
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(any(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
        .thenReturn(headerList);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any()))
        .thenReturn("File không đúng định dạng mẫu");
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    mrScheduleTelBusiness.importData(multipartFile);
  }

  @Test
  public void importData2() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FAIL);
    String filePath = "/temp";
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    Object[] objecttest = new Object[]{};
    File fileImport = new File(filePath);
    for (int i = 0; i < 1502; i++) {
      dataImportList.add(objecttest);
    }
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(fileImport,
                0,
                8,
                0,
                24,
                1000
            ))
        .thenReturn(dataImportList);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.scheduleId"))
        .thenReturn("MrScheduleTel.scheduleId");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.nationName"))
        .thenReturn("MrScheduleTel.nationName");
//    PowerMockito.when(I18n.getLanguage("MrScheduleTel.region"))
//        .thenReturn("MrScheduleTel.region");
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
    //      </editor-fold>
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    Object[] objectHeader = new Object[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("MrScheduleTel.scheduleId") + "*",
        I18n.getLanguage("MrScheduleTel.nationName"),
//        I18n.getLanguage("MrScheduleTel.region"),
        I18n.getLanguage("MrScheduleTel.arrayCode"),
        I18n.getLanguage("MrScheduleTel.networkType"),
        I18n.getLanguage("MrScheduleTel.deviceType"),
        I18n.getLanguage("MrScheduleTel.deviceCode"),
        I18n.getLanguage("MrScheduleTel.deviceName"),
        I18n.getLanguage("MrScheduleTel.nodeIp"),
        I18n.getLanguage("MrScheduleTel.vendor"),
        I18n.getLanguage("MrScheduleTel.mrId"),
        I18n.getLanguage("MrScheduleTel.woCode"),
        I18n.getLanguage("MrScheduleTel.coordinatingUnitHardName"),
        I18n.getLanguage("MrScheduleTel.cycle"),
        I18n.getLanguage("MrScheduleTel.cycleTypeName"),
        I18n.getLanguage("MrScheduleTel.woContent"),
        I18n.getLanguage("MrScheduleTel.lastDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateModifyStr") + "*",
        I18n.getLanguage("MrScheduleTel.updatedDateStr"),
        I18n.getLanguage("MrScheduleTel.scheduleType"),
        I18n.getLanguage("MrScheduleTel.station"),
        I18n.getLanguage("MrScheduleTel.userMrHard"),
        I18n.getLanguage("MrScheduleTel.mrConfirmName"),
        I18n.getLanguage("MrScheduleTel.mrComment")
    };
    List<Object[]> dataHeader = Mockito.spy(ArrayList.class);
    dataHeader.add(objectHeader);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                7,
                0,
                24,
                1000
            ))
        .thenReturn(dataHeader);
    //      </editor-fold>
    PowerMockito.when(I18n.getLanguage("common.importing.maxrow"))
        .thenReturn("Chỉ được import 1000 bản ghi trong một lần");
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    mrScheduleTelBusiness.importData(multipartFile);
  }

  @Test
  public void importData3() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<MrScheduleTelDTO> mrScheduleTelDTOArrayList = Mockito.spy(ArrayList.class);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);

    ResultInSideDto res = Mockito.spy(ResultInSideDto.class);
    res.setKey(RESULT.SUCCESS);

    MrDeviceDTO dtoMD = Mockito.spy(MrDeviceDTO.class);
    mrScheduleTelDTO.setScheduleId(2L);
    mrScheduleTelDTO.setMrHardCycle(null);
    mrScheduleTelDTO.setMrConfirm("2");
    mrScheduleTelDTO.setMrConfirmName("2");
    mrScheduleTelDTO.setCycleType("M");
    mrScheduleTelDTOArrayList.add(mrScheduleTelDTO);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FAIL);
    String filePath = "/temp";
    File fileImport = new File(filePath);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 2; i++) {
      Object[] objectTest = new Object[30];
      for (int j = 0; j < 30; j++) {
        if (j == 19) {
          objectTest[j] = "25/03/2019";
        } else {
          objectTest[j] = "2";
        }
      }
      dataImportList.add(objectTest);
    }
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(fileImport,
                0,
                8,
                0,
                24,
                1000
            ))
        .thenReturn(dataImportList);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.scheduleId"))
        .thenReturn("MrScheduleTel.scheduleId");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.nationName"))
        .thenReturn("MrScheduleTel.nationName");
//    PowerMockito.when(I18n.getLanguage("MrScheduleTel.region"))
//        .thenReturn("MrScheduleTel.region");
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
    //      </editor-fold>
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    Object[] objectHeader = new Object[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("MrScheduleTel.scheduleId") + "*",
        I18n.getLanguage("MrScheduleTel.nationName"),
//        I18n.getLanguage("MrScheduleTel.region"),
        I18n.getLanguage("MrScheduleTel.arrayCode"),
        I18n.getLanguage("MrScheduleTel.networkType"),
        I18n.getLanguage("MrScheduleTel.deviceType"),
        I18n.getLanguage("MrScheduleTel.deviceCode"),
        I18n.getLanguage("MrScheduleTel.deviceName"),
        I18n.getLanguage("MrScheduleTel.nodeIp"),
        I18n.getLanguage("MrScheduleTel.vendor"),
        I18n.getLanguage("MrScheduleTel.mrId"),
        I18n.getLanguage("MrScheduleTel.woCode"),
        I18n.getLanguage("MrScheduleTel.coordinatingUnitHardName"),
        I18n.getLanguage("MrScheduleTel.cycle"),
        I18n.getLanguage("MrScheduleTel.cycleTypeName"),
        I18n.getLanguage("MrScheduleTel.woContent"),
        I18n.getLanguage("MrScheduleTel.lastDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateModifyStr") + "*",
        I18n.getLanguage("MrScheduleTel.updatedDateStr"),
        I18n.getLanguage("MrScheduleTel.scheduleType"),
        I18n.getLanguage("MrScheduleTel.station"),
        I18n.getLanguage("MrScheduleTel.userMrHard"),
        I18n.getLanguage("MrScheduleTel.mrConfirmName"),
        I18n.getLanguage("MrScheduleTel.mrComment")
    };
    List<Object[]> dataHeader = Mockito.spy(ArrayList.class);
    dataHeader.add(objectHeader);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                7,
                0,
                24,
                1000
            ))
        .thenReturn(dataHeader);
    //      </editor-fold>
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    when(mrScheduleTelRepository.getDataExport(any(), anyString()))
        .thenReturn(mrScheduleTelDTOArrayList);
    when(mrDeviceRepository.findMrDeviceById(any())).thenReturn(dtoMD);
    when(mrScheduleTelRepository.updateMrScheduleTel(any())).thenReturn(res);

    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    mrScheduleTelDTO.setCycle("1");
    mrScheduleTelBusiness.importData(multipartFile);
  }

  @Test
  public void importData4() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<MrScheduleTelDTO> mrScheduleTelDTOArrayList = Mockito.spy(ArrayList.class);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);

    ResultInSideDto res = Mockito.spy(ResultInSideDto.class);
    res.setKey(RESULT.FAIL);

    MrDeviceDTO dtoMD = Mockito.spy(MrDeviceDTO.class);
    mrScheduleTelDTO.setScheduleId(2L);
    mrScheduleTelDTO.setMrHardCycle(null);
    mrScheduleTelDTO.setMrConfirm("2");
    mrScheduleTelDTO.setMrConfirmName("2");
    mrScheduleTelDTO.setCycleType("M");
    mrScheduleTelDTOArrayList.add(mrScheduleTelDTO);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FAIL);
    String filePath = "/temptt";
    File fileImport = new File(filePath);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 2; i++) {
      Object[] objectTest = new Object[30];
      for (int j = 0; j < 30; j++) {
        if (j == 19) {
          objectTest[j] = "25/03/2021";
        } else {
          objectTest[j] = "2";
        }
      }
      dataImportList.add(objectTest);
    }
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(fileImport,
                0,
                8,
                0,
                24,
                1000
            ))
        .thenReturn(dataImportList);
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    List<Object[]> dataHeader = Mockito.spy(ArrayList.class);
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.scheduleId"))
        .thenReturn("MrScheduleTel.scheduleId");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.nationName"))
        .thenReturn("MrScheduleTel.nationName");
//    PowerMockito.when(I18n.getLanguage("MrScheduleTel.region"))
//        .thenReturn("MrScheduleTel.region");
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
    //      </editor-fold>
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    Object[] objectHeader = new Object[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("MrScheduleTel.scheduleId") + "*",
        I18n.getLanguage("MrScheduleTel.nationName"),
//        I18n.getLanguage("MrScheduleTel.region"),
        I18n.getLanguage("MrScheduleTel.arrayCode"),
        I18n.getLanguage("MrScheduleTel.networkType"),
        I18n.getLanguage("MrScheduleTel.deviceType"),
        I18n.getLanguage("MrScheduleTel.deviceCode"),
        I18n.getLanguage("MrScheduleTel.deviceName"),
        I18n.getLanguage("MrScheduleTel.nodeIp"),
        I18n.getLanguage("MrScheduleTel.vendor"),
        I18n.getLanguage("MrScheduleTel.mrId"),
        I18n.getLanguage("MrScheduleTel.woCode"),
        I18n.getLanguage("MrScheduleTel.coordinatingUnitHardName"),
        I18n.getLanguage("MrScheduleTel.cycle"),
        I18n.getLanguage("MrScheduleTel.cycleTypeName"),
        I18n.getLanguage("MrScheduleTel.woContent"),
        I18n.getLanguage("MrScheduleTel.lastDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateModifyStr") + "*",
        I18n.getLanguage("MrScheduleTel.updatedDateStr"),
        I18n.getLanguage("MrScheduleTel.scheduleType"),
        I18n.getLanguage("MrScheduleTel.station"),
        I18n.getLanguage("MrScheduleTel.userMrHard"),
        I18n.getLanguage("MrScheduleTel.mrConfirmName"),
        I18n.getLanguage("MrScheduleTel.mrComment")
    };
    dataHeader.add(objectHeader);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                7,
                0,
                24,
                1000
            ))
        .thenReturn(dataHeader);
    //      </editor-fold>
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    when(mrScheduleTelRepository.getDataExport(any(), anyString()))
        .thenReturn(mrScheduleTelDTOArrayList);
    when(mrDeviceRepository.findMrDeviceById(any())).thenReturn(dtoMD);
    when(mrScheduleTelRepository.updateMrScheduleTel(any())).thenReturn(res);

    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    mrScheduleTelDTO.setCycle("1");
    mrScheduleTelBusiness.importData(multipartFile);
  }

  @Test
  public void importData4_1() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    List<MrScheduleTelDTO> mrScheduleTelDTOArrayList = Mockito.spy(ArrayList.class);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    ResultInSideDto res = Mockito.spy(ResultInSideDto.class);
    res.setKey(RESULT.FAIL);

    MrDeviceDTO dtoMD = Mockito.spy(MrDeviceDTO.class);
    mrScheduleTelDTO.setScheduleId(2L);
    mrScheduleTelDTO.setMrHardCycle(null);
    mrScheduleTelDTO.setMrConfirm("2");
    mrScheduleTelDTO.setMrConfirmName("2");
    mrScheduleTelDTO.setCycleType("M");
    mrScheduleTelDTOArrayList.add(mrScheduleTelDTO);
//    MrScheduleTelDTO objUpdate = Mockito.spy(MrScheduleTelDTO.class);
    PowerMockito.mockStatic(FileUtils.class);
//    PowerMockito.mockStatic(DataUtil.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FAIL);
    String filePath = "/tempsss";
    File fileImport = new File(filePath);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 3; i++) {
      Object[] objectTest = new Object[30];
      for (int j = 0; j < 30; j++) {
        if (j == 19) {
          objectTest[j] = "25/03/2021";
        } else {
          objectTest[j] = "2";
        }
      }
      dataImportList.add(objectTest);
    }
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(fileImport,
                0,
                8,
                0,
                24,
                1000
            ))
        .thenReturn(dataImportList);
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    List<Object[]> dataHeader = Mockito.spy(ArrayList.class);
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.scheduleId"))
        .thenReturn("MrScheduleTel.scheduleId");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.nationName"))
        .thenReturn("MrScheduleTel.nationName");
//    PowerMockito.when(I18n.getLanguage("MrScheduleTel.region"))
//        .thenReturn("MrScheduleTel.region");
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
    //      </editor-fold>
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    Object[] objectHeader = new Object[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("MrScheduleTel.scheduleId") + "*",
        I18n.getLanguage("MrScheduleTel.nationName"),
//        I18n.getLanguage("MrScheduleTel.region"),
        I18n.getLanguage("MrScheduleTel.arrayCode"),
        I18n.getLanguage("MrScheduleTel.networkType"),
        I18n.getLanguage("MrScheduleTel.deviceType"),
        I18n.getLanguage("MrScheduleTel.deviceCode"),
        I18n.getLanguage("MrScheduleTel.deviceName"),
        I18n.getLanguage("MrScheduleTel.nodeIp"),
        I18n.getLanguage("MrScheduleTel.vendor"),
        I18n.getLanguage("MrScheduleTel.mrId"),
        I18n.getLanguage("MrScheduleTel.woCode"),
        I18n.getLanguage("MrScheduleTel.coordinatingUnitHardName"),
        I18n.getLanguage("MrScheduleTel.cycle"),
        I18n.getLanguage("MrScheduleTel.cycleTypeName"),
        I18n.getLanguage("MrScheduleTel.woContent"),
        I18n.getLanguage("MrScheduleTel.lastDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateModifyStr") + "*",
        I18n.getLanguage("MrScheduleTel.updatedDateStr"),
        I18n.getLanguage("MrScheduleTel.scheduleType"),
        I18n.getLanguage("MrScheduleTel.station"),
        I18n.getLanguage("MrScheduleTel.userMrHard"),
        I18n.getLanguage("MrScheduleTel.mrConfirmName"),
        I18n.getLanguage("MrScheduleTel.mrComment")
    };
    dataHeader.add(objectHeader);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                7,
                0,
                24,
                1000
            ))
        .thenReturn(dataHeader);
    //      </editor-fold>
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    when(mrScheduleTelRepository.getDataExport(any(), anyString()))
        .thenReturn(mrScheduleTelDTOArrayList);
    when(mrDeviceRepository.findMrDeviceById(any())).thenReturn(dtoMD);
    when(mrScheduleTelRepository.updateMrScheduleTel(any())).thenReturn(res);

    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);

    mrScheduleTelDTO.setCycle("1");
    mrScheduleTelBusiness.importData(multipartFile);
  }

  @Test
  public void importData5() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FAIL);
    String filePath = "/tempp";
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    Object[] objecttest = new Object[30];
    for (int i = 0; i < 30; i++) {
      if (i == 19) {
        objecttest[i] = "25/03/2021";
      } else if (i != 24) {
        objecttest[i] = "1";
      }
    }
    for (int i = 0; i < 3; i++) {
      dataImportList.add(objecttest);
    }
    File fileImport = new File(filePath);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(fileImport,
                0,
                8,
                0,
                24,
                1000
            ))
        .thenReturn(dataImportList);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.scheduleId"))
        .thenReturn("MrScheduleTel.scheduleId");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.nationName"))
        .thenReturn("MrScheduleTel.nationName");
//    PowerMockito.when(I18n.getLanguage("MrScheduleTel.region"))
//        .thenReturn("MrScheduleTel.region");
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
    // </editor-fold>
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    Object[] objectHeader = new Object[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("MrScheduleTel.scheduleId") + "*",
        I18n.getLanguage("MrScheduleTel.nationName"),
//        I18n.getLanguage("MrScheduleTel.region"),
        I18n.getLanguage("MrScheduleTel.arrayCode"),
        I18n.getLanguage("MrScheduleTel.networkType"),
        I18n.getLanguage("MrScheduleTel.deviceType"),
        I18n.getLanguage("MrScheduleTel.deviceCode"),
        I18n.getLanguage("MrScheduleTel.deviceName"),
        I18n.getLanguage("MrScheduleTel.nodeIp"),
        I18n.getLanguage("MrScheduleTel.vendor"),
        I18n.getLanguage("MrScheduleTel.mrId"),
        I18n.getLanguage("MrScheduleTel.woCode"),
        I18n.getLanguage("MrScheduleTel.coordinatingUnitHardName"),
        I18n.getLanguage("MrScheduleTel.cycle"),
        I18n.getLanguage("MrScheduleTel.cycleTypeName"),
        I18n.getLanguage("MrScheduleTel.woContent"),
        I18n.getLanguage("MrScheduleTel.lastDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateModifyStr") + "*",
        I18n.getLanguage("MrScheduleTel.updatedDateStr"),
        I18n.getLanguage("MrScheduleTel.scheduleType"),
        I18n.getLanguage("MrScheduleTel.station"),
        I18n.getLanguage("MrScheduleTel.userMrHard"),
        I18n.getLanguage("MrScheduleTel.mrConfirmName"),
        I18n.getLanguage("MrScheduleTel.mrComment")
    };
    List<Object[]> dataHeader = Mockito.spy(ArrayList.class);
    dataHeader.add(objectHeader);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                7,
                0,
                24,
                1000
            ))
        .thenReturn(dataHeader);
    // </editor-fold>
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleIdStr("1");
    mrScheduleTelDTO.setScheduleId(1L);
    List<MrScheduleTelDTO> lstMrScheduleTels = Mockito.spy(ArrayList.class);
    lstMrScheduleTels.add(mrScheduleTelDTO);
    PowerMockito
        .when(mrScheduleTelRepository.getDataExport(any(MrScheduleTelDTO.class), anyString()))
        .thenReturn(lstMrScheduleTels);
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("2");
    usersInsideDto.setUserId(1L);
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    PowerMockito.when(userRepository.getUserDTOByUserName(anyString())).thenReturn(usersInsideDto);
    PowerMockito.when(I18n.getValidation(anyString())).thenReturn("HAVE_ERROR");
    PowerMockito.when(I18n.getLanguage("import.common.fail"))
        .thenReturn("Import không thành công");
    mrScheduleTelBusiness.importData(multipartFile);
  }

  @Test
  public void importData6() throws Exception {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.FAIL);
    String filePath = "/temps";
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    Object[] objecttest = new Object[]{};
    File fileImport = new File(filePath);
    dataImportList.add(objecttest);
    dataImportList.clear();
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(fileImport,
                0,
                8,
                0,
                24,
                1000
            ))
        .thenReturn(dataImportList);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.scheduleId"))
        .thenReturn("MrScheduleTel.scheduleId");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.nationName"))
        .thenReturn("MrScheduleTel.nationName");
//    PowerMockito.when(I18n.getLanguage("MrScheduleTel.region"))
//        .thenReturn("MrScheduleTel.region");
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
    //      </editor-fold>
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    Object[] objectHeader = new Object[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("MrScheduleTel.scheduleId") + "*",
        I18n.getLanguage("MrScheduleTel.nationName"),
//        I18n.getLanguage("MrScheduleTel.region"),
        I18n.getLanguage("MrScheduleTel.arrayCode"),
        I18n.getLanguage("MrScheduleTel.networkType"),
        I18n.getLanguage("MrScheduleTel.deviceType"),
        I18n.getLanguage("MrScheduleTel.deviceCode"),
        I18n.getLanguage("MrScheduleTel.deviceName"),
        I18n.getLanguage("MrScheduleTel.nodeIp"),
        I18n.getLanguage("MrScheduleTel.vendor"),
        I18n.getLanguage("MrScheduleTel.mrId"),
        I18n.getLanguage("MrScheduleTel.woCode"),
        I18n.getLanguage("MrScheduleTel.coordinatingUnitHardName"),
        I18n.getLanguage("MrScheduleTel.cycle"),
        I18n.getLanguage("MrScheduleTel.cycleTypeName"),
        I18n.getLanguage("MrScheduleTel.woContent"),
        I18n.getLanguage("MrScheduleTel.lastDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateModifyStr") + "*",
        I18n.getLanguage("MrScheduleTel.updatedDateStr"),
        I18n.getLanguage("MrScheduleTel.scheduleType"),
        I18n.getLanguage("MrScheduleTel.station"),
        I18n.getLanguage("MrScheduleTel.userMrHard"),
        I18n.getLanguage("MrScheduleTel.mrConfirmName"),
        I18n.getLanguage("MrScheduleTel.mrComment")
    };
    List<Object[]> dataHeader = Mockito.spy(ArrayList.class);
    dataHeader.add(objectHeader);
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFile(
                fileImport,
                0,
                7,
                0,
                24,
                1000
            ))
        .thenReturn(dataHeader);
    //      </editor-fold>
    PowerMockito.when(FileUtils.saveTempFile(any(), any(), any())).thenReturn(filePath);
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    PowerMockito.when(I18n.getLanguage("common.searh.nodata"))
        .thenReturn("không có dữ liệu");
    mrScheduleTelBusiness.importData(multipartFile);
  }

  @Test
  public void validate() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.ERROR);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setNextDateModify(null);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Ngày dự kiến BD không được trống");
    mrScheduleTelBusiness.validate(mrScheduleTelDTO, true);
  }

  @Test
  public void validate1() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.ERROR);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setNextDateModify(DateTimeUtils.convertStringToDate("27/03/2019 12:12:12"));

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Ngày dự kiến BD phải lớn hơn ngày hiện tại");
    mrScheduleTelBusiness.validate(mrScheduleTelDTO, true);
  }

  @Test
  public void validate2() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.ERROR);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setNextDateModify(DateTimeUtils.convertStringToDate("27/02/2020 12:12:12"));

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Ngày dự kiến BD phải lớn hơn ngày hiện tại");
    mrScheduleTelBusiness.validate(mrScheduleTelDTO, true);
  }

  @Test
  public void validate3() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.ERROR);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setNextDateModify(DateTimeUtils.convertStringToDate("26/03/2020 12:12:12"));

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Ngày dự kiến BD phải lớn hơn ngày hiện tại");
    mrScheduleTelBusiness.validate(mrScheduleTelDTO, true);
  }

  @Test
  public void validate4() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.ERROR);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.DATE, 1);
    mrScheduleTelDTO.setNextDateModify(cal.getTime());
    String a = "";
    for (int i = 0; i < 1002; i++) {
      a += "t";
    }
    mrScheduleTelDTO.setMrComment("" + a);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Ghi chú không được lớn hơn 1000 ký tự");
    mrScheduleTelBusiness.validate(mrScheduleTelDTO, true);
  }

  @Test
  public void validate5() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.ERROR);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.DATE, 1);
    mrScheduleTelDTO.setNextDateModify(cal.getTime());
    List<String> listCommen = Mockito.spy(ArrayList.class);
    String a = "";
    for (int i = 0; i < 999; i++) {
      a += "t";
    }
    mrScheduleTelDTO.setMrComment("" + a);
    mrScheduleTelBusiness.validate(mrScheduleTelDTO, true);
  }

  @Test
  public void validateImportInfo() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleIdStr(null);
    List<MrScheduleTelDTO> listDto = Mockito.spy(List.class);
    String type = "test";

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("ID bản ghi không được để trống");

    mrScheduleTelBusiness.validateImportInfo(mrScheduleTelDTO, listDto, type);
  }

  @Test
  public void validateImportInfo1() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleIdStr("test1");
    mrScheduleTelDTO.setMrConfirm("6");
    mrScheduleTelDTO.setCrIdStr(null);
    List<MrScheduleTelDTO> listDto = Mockito.spy(List.class);
    String type = "S";

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Mã CR là bắt buộc nhập vói lý do không bảo dưỡng 'Nâng Cấp'");

    mrScheduleTelBusiness.validateImportInfo(mrScheduleTelDTO, listDto, type);
  }

  @Test
  public void validateImportInfo2() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleIdStr("test2");
    mrScheduleTelDTO.setMrConfirm("5");
    mrScheduleTelDTO.setCrIdStr("test");
    List<MrScheduleTelDTO> listDto = Mockito.spy(List.class);
    String type = "S";

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("CrId không hợp lệ'");
    mrScheduleTelBusiness.validateImportInfo(mrScheduleTelDTO, listDto, type);
  }

  @Test
  public void validateImportInfo3() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleIdStr("test3");
    mrScheduleTelDTO.setMrConfirm("5");
    mrScheduleTelDTO.setCrIdStr("3");
    List<MrScheduleTelDTO> listDto = Mockito.spy(List.class);
    String type = "S";

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Ngày dự kiên BD không được để trống");

    mrScheduleTelBusiness.validateImportInfo(mrScheduleTelDTO, listDto, type);
  }

  @Test
  public void validateImportInfo4() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(1L);
    mrScheduleTelDTO.setScheduleIdStr("test3");
    mrScheduleTelDTO.setMrConfirm("5");
    mrScheduleTelDTO.setCrIdStr("2");
    mrScheduleTelDTO.setNextDateModifyStr("test");
    mrScheduleTelDTO.setNextDateModify(DateTimeUtils.convertStringToDate("26/03/2019 12:12:12"));
    List<MrScheduleTelDTO> listDto = Mockito.spy(List.class);
    String type = "S";

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Ngày dự kiến BD phải lớn hơn ngày hiện tại");

    mrScheduleTelBusiness.validateImportInfo(mrScheduleTelDTO, listDto, type);
  }

  @Test
  public void validateImportInfo5() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(1L);
    mrScheduleTelDTO.setScheduleIdStr("test3");
    mrScheduleTelDTO.setMrConfirm("5");
    mrScheduleTelDTO.setCrIdStr("2");
    mrScheduleTelDTO.setNextDateModifyStr("test");
    mrScheduleTelDTO.setNextDateModify(DateTimeUtils.convertStringToDate("26/02/2020 12:12:12"));
    List<MrScheduleTelDTO> listDto = Mockito.spy(List.class);
    String type = "S";

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Ngày dự kiến BD phải lớn hơn ngày hiện tại");

    mrScheduleTelBusiness.validateImportInfo(mrScheduleTelDTO, listDto, type);
  }

  @Test
  public void validateImportInfo6() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(1L);
    mrScheduleTelDTO.setScheduleIdStr("test3");
    mrScheduleTelDTO.setMrConfirm("5");
    mrScheduleTelDTO.setCrIdStr("2");
    mrScheduleTelDTO.setNextDateModifyStr("test");
    mrScheduleTelDTO.setNextDateModify(DateTimeUtils.convertStringToDate("26/03/2020 12:12:12"));
    List<MrScheduleTelDTO> listDto = Mockito.spy(List.class);
    String type = "S";

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Ngày dự kiến BD phải lớn hơn ngày hiện tại");

    mrScheduleTelBusiness.validateImportInfo(mrScheduleTelDTO, listDto, type);
  }

  @Test
  public void validateImportInfo7() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(1L);
    mrScheduleTelDTO.setScheduleIdStr("test3");
    mrScheduleTelDTO.setMrConfirm("5");
    mrScheduleTelDTO.setCrIdStr("2");
    mrScheduleTelDTO.setNextDateModifyStr("test");
    mrScheduleTelDTO.setNextDateModify(null);
    mrScheduleTelDTO.setMrConfirmName("dungtest");
    mrScheduleTelDTO.setMrConfirm(null);
    List<MrScheduleTelDTO> listDto = Mockito.spy(List.class);
    String type = "S";

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Lý do không BD không hợp lệ");

    mrScheduleTelBusiness.validateImportInfo(mrScheduleTelDTO, listDto, type);
  }

  @Test
  public void validateImportInfo8() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(1L);
    mrScheduleTelDTO.setScheduleIdStr("test3");
    mrScheduleTelDTO.setMrConfirm("5");
    mrScheduleTelDTO.setCrIdStr("2");
    mrScheduleTelDTO.setNextDateModifyStr("test");
    mrScheduleTelDTO.setNextDateModify(null);
    mrScheduleTelDTO.setMrConfirmName(null);
    mrScheduleTelDTO.setMrConfirm(null);
    String a = "";
    for (int i = 0; i < 1002; i++) {
      a += "n";
    }
    mrScheduleTelDTO.setMrComment(a);
    List<MrScheduleTelDTO> listDto = Mockito.spy(List.class);
    String type = "S";

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Ghi chú không được lớn hơn 1000 ký tự");

    mrScheduleTelBusiness.validateImportInfo(mrScheduleTelDTO, listDto, type);
  }

  @Test
  public void validateImportInfo9() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(1L);
    mrScheduleTelDTO.setScheduleIdStr("test3");
    mrScheduleTelDTO.setMrConfirm("5");
    mrScheduleTelDTO.setCrIdStr("2");
    mrScheduleTelDTO.setNextDateModifyStr("test");
    mrScheduleTelDTO.setNextDateModify(null);
    mrScheduleTelDTO.setMrConfirmName(null);
    mrScheduleTelDTO.setMrConfirm(null);
    mrScheduleTelDTO.setUserMrHard("dunglv");
    String a = "";
    for (int i = 0; i < 100; i++) {
      a += "n";
    }
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto.setUsername("dunglv3");
    when(userRepository.getUserDTOByUserName(any())).thenReturn(usersInsideDto);

    mrScheduleTelDTO.setMrComment(a);
    List<MrScheduleTelDTO> listDto = Mockito.spy(List.class);
    String type = "S";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Người tạo không hợp lệ");
    mrScheduleTelBusiness.validateImportInfo(mrScheduleTelDTO, listDto, type);
  }

  @Test
  public void validateImportInfo10() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(1L);
    mrScheduleTelDTO.setScheduleIdStr("test3");
    mrScheduleTelDTO.setMrConfirm("5");
    mrScheduleTelDTO.setCrIdStr("2");
    mrScheduleTelDTO.setNextDateModifyStr("test");
    mrScheduleTelDTO.setNextDateModify(null);
    mrScheduleTelDTO.setMrConfirmName(null);
    mrScheduleTelDTO.setMrConfirm(null);
    mrScheduleTelDTO.setUserMrHard("dunglv");
    String a = "";
    for (int i = 0; i < 100; i++) {
      a += "n";
    }
    UsersInsideDto usersInsideDto = Mockito.spy(UsersInsideDto.class);
    usersInsideDto = null;
    when(userRepository.getUserDTOByUserName(any())).thenReturn(usersInsideDto);

    mrScheduleTelDTO.setMrComment(a);
    List<MrScheduleTelDTO> listDto = Mockito.spy(List.class);
    String type = "S";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Người tạo không hợp lệ");
    mrScheduleTelBusiness.validateImportInfo(mrScheduleTelDTO, listDto, type);
  }

  @Test
  public void validateImportInfo11() {
    MrScheduleTelDTO mrScheduleTelDTO1 = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO1.setMrId(1L);
    Map<Long, MrScheduleTelDTO> mapDataImport = new HashMap<>();
    mapDataImport.put(2L, mrScheduleTelDTO1);
    ReflectionTestUtils.setField(mrScheduleTelBusiness, "mapDataImport",
        mapDataImport);

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(2L);
    mrScheduleTelDTO.setScheduleIdStr("test3");
    mrScheduleTelDTO.setMrConfirm("5");
    mrScheduleTelDTO.setCrIdStr("2");
    mrScheduleTelDTO.setNextDateModifyStr("test");
    mrScheduleTelDTO.setNextDateModify(null);
    mrScheduleTelDTO.setMrConfirmName(null);
    mrScheduleTelDTO.setMrConfirm(null);
    mrScheduleTelDTO.setUserMrHard(null);
    mrScheduleTelDTO.setMrId(2L);
    String a = "";
    for (int i = 0; i < 100; i++) {
      a += "n";
    }

    mrScheduleTelDTO.setMrComment(a);
    List<MrScheduleTelDTO> listDto = Mockito.spy(List.class);
    String type = "S";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Người tạo không hợp lệ");
    mrScheduleTelBusiness.validateImportInfo(mrScheduleTelDTO, listDto, type);
  }

  @Test
  public void validateImportInfo12() {
    MrScheduleTelDTO mrScheduleTelDTO1 = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO1.setMrId(null);
    Map<Long, MrScheduleTelDTO> mapDataImport = new HashMap<>();
    mapDataImport.put(2L, mrScheduleTelDTO1);
    ReflectionTestUtils.setField(mrScheduleTelBusiness, "mapDataImport",
        mapDataImport);

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(2L);
    mrScheduleTelDTO.setScheduleIdStr("test3");
    mrScheduleTelDTO.setMrConfirm("5");
    mrScheduleTelDTO.setCrIdStr("2");
    mrScheduleTelDTO.setNextDateModifyStr("test");
    mrScheduleTelDTO.setNextDateModify(null);
    mrScheduleTelDTO.setMrConfirmName(null);
    mrScheduleTelDTO.setMrConfirm(null);
    mrScheduleTelDTO.setUserMrHard(null);
    mrScheduleTelDTO.setMrId(2L);
    String a = "";
    for (int i = 0; i < 100; i++) {
      a += "n";
    }

    mrScheduleTelDTO.setMrComment(a);
    List<MrScheduleTelDTO> listDto = Mockito.spy(List.class);
    String type = "S";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Người tạo không hợp lệ");
    mrScheduleTelBusiness.validateImportInfo(mrScheduleTelDTO, listDto, type);
  }

  @Test
  public void setMrConfirm() {
    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    List<MrConfigDTO> lstMrConfigDTO = Mockito.spy(ArrayList.class);
    lstMrConfigDTO.add(mrConfigDTO);
    when(mrScheduleTelRepository.getConfigByGroup(any())).thenReturn(lstMrConfigDTO);
    mrScheduleTelBusiness.setMrConfirm();
  }

  @Test
  public void validateDuplicate() {
    MrScheduleTelDTO dto = Mockito.spy(MrScheduleTelDTO.class);
    dto.setScheduleId(2L);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(2L);
    List<MrScheduleTelDTO> lstoExportDTOS = Mockito.spy(ArrayList.class);
    lstoExportDTOS.add(mrScheduleTelDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Bản ghi bị trùng với bản ghi ở vị trí thứ 0");
    mrScheduleTelBusiness.validateDuplicate(dto, lstoExportDTOS);
  }

  @Test
  public void exportFileEx() throws Exception {
    String key = "RESULT_IMPORT";
    String type = "H";
    List<MrScheduleTelDTO> lstData = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(I18n.class);
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    PowerMockito.when(I18n.getLanguage(any()))
        .thenReturn("Ngày Export: {0}");
    mrScheduleTelBusiness.exportFileEx(lstData, key, type);
  }

  @Test
  public void exportFileEx1() throws Exception {
    String key = "RESULT_IMPORT_ELSE";
    String type = "H";
    List<MrScheduleTelDTO> lstData = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(I18n.class);
    File fileExportSuccess = new File("./test_junit11/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    PowerMockito.when(I18n.getLanguage(any()))
        .thenReturn("Ngày Export: {0}");
    mrScheduleTelBusiness.exportFileEx(lstData, key, type);
  }

  @Test
  public void exportFileEx2() throws Exception {
    String key = "RESULT_IMPORT";
    String type = "S";
    List<MrScheduleTelDTO> lstData = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(I18n.class);
    File fileExportSuccess = new File("./test_junit4/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    PowerMockito.when(I18n.getLanguage(any()))
        .thenReturn("Ngày Export: {0}");
    mrScheduleTelBusiness.exportFileEx(lstData, key, type);
  }

  @Test
  public void exportFileEx3() throws Exception {
    String key = "RESULT_IMPORT_ELSE";
    String type = "S";
    List<MrScheduleTelDTO> lstData = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(I18n.class);
    File fileExportSuccess = new File("./test_junit2/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    PowerMockito.when(I18n.getLanguage(any()))
        .thenReturn("Ngày Export: {0}");
    mrScheduleTelBusiness.exportFileEx(lstData, key, type);
  }

  @Test
  public void resultList() {
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setCoordinatingUnitHard("4");
    List<MrScheduleTelDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleTelDTO);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(2L);
    List<WoCdGroupInsideDTO> lstCd = Mockito.spy(ArrayList.class);
    lstCd.add(woCdGroupInsideDTO);

    String type = "H";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any()))
        .thenReturn("Bảo dưỡng cứng");
    when(woCategoryServiceProxy.getListCdGroupByUser(any())).thenReturn(lstCd);

    mrScheduleTelBusiness.resultList(lst, type);
  }

  @Test
  public void resultList1() {
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setCoordinatingUnitHard("3");
    List<MrScheduleTelDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleTelDTO);

    WoCdGroupInsideDTO woCdGroupInsideDTO = Mockito.spy(WoCdGroupInsideDTO.class);
    woCdGroupInsideDTO.setWoGroupId(2L);
    List<WoCdGroupInsideDTO> lstCd = Mockito.spy(ArrayList.class);
    lstCd.add(woCdGroupInsideDTO);

    String type = "S";
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any()))
        .thenReturn("Ngày Export: {0}");
    when(woCategoryServiceProxy.getListCdGroupByUser(any())).thenReturn(lstCd);

    mrScheduleTelBusiness.resultList(lst, type);
  }

  @Test
  public void exportSoftData() throws Exception {
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    List<MrScheduleTelDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleTelDTO);
    File fileExportSuccess = new File("./test_junit2/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    when(mrScheduleTelRepository
        .getDataExport(any(), anyString())).thenReturn(lst);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any()))
        .thenReturn("Bảo dưỡng mềm");
    mrScheduleTelBusiness.exportSoftData(mrScheduleTelDTO);
  }

  @Test
  public void importSoftData() throws Exception {
    MultipartFile multipartFile = null;
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any()))
        .thenReturn("Chưa chọn file import");
    mrScheduleTelBusiness.importSoftData(multipartFile);
  }

  @Test
  public void importSoftData0() throws Exception {
    PowerMockito.mockStatic(FileUtils.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    PowerMockito.mockStatic(CommonImport.class);
    String filePath = "/test_junit2/test.txt";
    File fileImport = new File(filePath);
    PowerMockito.mockStatic(I18n.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    Object[] objecttest = new Object[]{};
    for (int i = 0; i < 1503; i++) {
      dataImportList.add(objecttest);
    }
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFileNew(fileImport,
                0,
                8,
                0,
                26,
                1000
            ))
        .thenReturn(dataImportList);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.scheduleId"))
        .thenReturn("MrScheduleTel.scheduleId");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.nationName"))
        .thenReturn("MrScheduleTel.nationName");
//    PowerMockito.when(I18n.getLanguage("MrScheduleTel.region"))
//        .thenReturn("MrScheduleTel.region");
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
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.crId"))
        .thenReturn("MrScheduleTel.crId");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.implementUnitName"))
        .thenReturn("MrScheduleTel.implementUnitName");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.checkingUnitName"))
        .thenReturn("MrScheduleTel.checkingUnitName");
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
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.groupCode"))
        .thenReturn("MrScheduleTel.groupCode");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.impactNode"))
        .thenReturn("MrScheduleTel.impactNode");
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

    Object[] objectHeader = new Object[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("MrScheduleTel.scheduleId"),
        I18n.getLanguage("MrScheduleTel.nationName"),
//        I18n.getLanguage("MrScheduleTel.region"),
        I18n.getLanguage("MrScheduleTel.arrayCode"),
        I18n.getLanguage("MrScheduleTel.networkType"),
        I18n.getLanguage("MrScheduleTel.deviceType"),
        I18n.getLanguage("MrScheduleTel.deviceCode"),
        I18n.getLanguage("MrScheduleTel.deviceName"),
        I18n.getLanguage("MrScheduleTel.nodeIp"),
        I18n.getLanguage("MrScheduleTel.vendor"),
        I18n.getLanguage("MrScheduleTel.mrId"),//11
        I18n.getLanguage("MrScheduleTel.crId"),
        I18n.getLanguage("MrScheduleTel.implementUnitName"),
        I18n.getLanguage("MrScheduleTel.checkingUnitName"),
        I18n.getLanguage("MrScheduleTel.cycle"),
        I18n.getLanguage("MrScheduleTel.cycleTypeName"),
        I18n.getLanguage("MrScheduleTel.woContent"),
        I18n.getLanguage("MrScheduleTel.lastDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateModifyStr"),
        I18n.getLanguage("MrScheduleTel.updatedDateStr"),
        I18n.getLanguage("MrScheduleTel.groupCode"),
        I18n.getLanguage("MrScheduleTel.impactNode"),
        I18n.getLanguage("MrScheduleTel.scheduleType"),
        I18n.getLanguage("MrScheduleTel.station"),
        I18n.getLanguage("MrScheduleTel.mrConfirmName"),
        I18n.getLanguage("MrScheduleTel.mrComment")
    };
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objectHeader);
    PowerMockito
        .when(CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            7,
            0,
            26,
            1000
        ))
        .thenReturn(headerList);
    //</editor-fold>
    when(FileUtils
        .saveTempFile(any(), any(), anyString())).thenReturn(filePath);
    mrScheduleTelBusiness.importSoftData(multipartFile);
  }

  @Test
  public void importSoftData1() throws Exception {
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(2L);
    List<MrScheduleTelDTO> mrScheduleTelDTOArrayList = Mockito.spy(ArrayList.class);
    mrScheduleTelDTOArrayList.add(mrScheduleTelDTO);

    ResultInSideDto res = Mockito.spy(ResultInSideDto.class);
    res.setKey(RESULT.SUCCESS);

    PowerMockito.mockStatic(FileUtils.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    PowerMockito.mockStatic(CommonImport.class);
    String filePath = "/test_junit2/test.txt";
    File fileImport = new File(filePath);
    PowerMockito.mockStatic(I18n.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 3; i++) {
      Object[] objectTest = new Object[30];
      for (int j = 0; j < 30; j++) {
        if (j == 19) {
          objectTest[j] = "25/03/2019";
        } else {
          objectTest[j] = "2";
        }
      }
      dataImportList.add(objectTest);
    }
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFileNew(fileImport,
                0,
                8,
                0,
                26,
                1000
            ))
        .thenReturn(dataImportList);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.scheduleId"))
        .thenReturn("MrScheduleTel.scheduleId");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.nationName"))
        .thenReturn("MrScheduleTel.nationName");
//    PowerMockito.when(I18n.getLanguage("MrScheduleTel.region"))
//        .thenReturn("MrScheduleTel.region");
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
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.crId"))
        .thenReturn("MrScheduleTel.crId");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.implementUnitName"))
        .thenReturn("MrScheduleTel.implementUnitName");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.checkingUnitName"))
        .thenReturn("MrScheduleTel.checkingUnitName");
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
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.groupCode"))
        .thenReturn("MrScheduleTel.groupCode");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.impactNode"))
        .thenReturn("MrScheduleTel.impactNode");
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

    Object[] objectHeader = new Object[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("MrScheduleTel.scheduleId"),
        I18n.getLanguage("MrScheduleTel.nationName"),
//        I18n.getLanguage("MrScheduleTel.region"),
        I18n.getLanguage("MrScheduleTel.arrayCode"),
        I18n.getLanguage("MrScheduleTel.networkType"),
        I18n.getLanguage("MrScheduleTel.deviceType"),
        I18n.getLanguage("MrScheduleTel.deviceCode"),
        I18n.getLanguage("MrScheduleTel.deviceName"),
        I18n.getLanguage("MrScheduleTel.nodeIp"),
        I18n.getLanguage("MrScheduleTel.vendor"),
        I18n.getLanguage("MrScheduleTel.mrId"),//11
        I18n.getLanguage("MrScheduleTel.crId"),
        I18n.getLanguage("MrScheduleTel.implementUnitName"),
        I18n.getLanguage("MrScheduleTel.checkingUnitName"),
        I18n.getLanguage("MrScheduleTel.cycle"),
        I18n.getLanguage("MrScheduleTel.cycleTypeName"),
        I18n.getLanguage("MrScheduleTel.woContent"),
        I18n.getLanguage("MrScheduleTel.lastDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateModifyStr"),
        I18n.getLanguage("MrScheduleTel.updatedDateStr"),
        I18n.getLanguage("MrScheduleTel.groupCode"),
        I18n.getLanguage("MrScheduleTel.impactNode"),
        I18n.getLanguage("MrScheduleTel.scheduleType"),
        I18n.getLanguage("MrScheduleTel.station"),
        I18n.getLanguage("MrScheduleTel.mrConfirmName"),
        I18n.getLanguage("MrScheduleTel.mrComment")
    };
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objectHeader);
    PowerMockito
        .when(CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            7,
            0,
            26,
            1000
        ))
        .thenReturn(headerList);
    //</editor-fold>
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    when(FileUtils
        .saveTempFile(any(), any(), anyString())).thenReturn(filePath);
    when(mrScheduleTelRepository
        .getListMrSheduleTelByIdForImport(any())).thenReturn(mrScheduleTelDTOArrayList);
    PowerMockito.when(I18n.getLanguage("mrScheduleTel.networkType.viba"))
        .thenReturn("Link/Tuyến viba");
    when(mrScheduleTelRepository.updateMrScheduleTel(any())).thenReturn(res);
    mrScheduleTelBusiness.importSoftData(multipartFile);
  }

  @Test
  public void importSoftData2() throws Exception {
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(2L);
    List<MrScheduleTelDTO> mrScheduleTelDTOArrayList = Mockito.spy(ArrayList.class);
    mrScheduleTelDTOArrayList.add(mrScheduleTelDTO);

    MrDeviceDTO mrDeviceDTO = Mockito.spy(MrDeviceDTO.class);

    ResultInSideDto res = Mockito.spy(ResultInSideDto.class);
    res.setKey(RESULT.SUCCESS);

    PowerMockito.mockStatic(FileUtils.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    PowerMockito.mockStatic(CommonImport.class);
    String filePath = "/test_junit2/test.txt";
    File fileImport = new File(filePath);
    PowerMockito.mockStatic(I18n.class);
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 4; i++) {
      Object[] objectTest = new Object[30];
      for (int j = 0; j < 30; j++) {
        if (j == 19) {
          objectTest[j] = "25/03/2019";
        } else {
          objectTest[j] = "2";
        }
      }
      dataImportList.add(objectTest);
    }
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFileNew(fileImport,
                0,
                8,
                0,
                26,
                1000
            ))
        .thenReturn(dataImportList);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.scheduleId"))
        .thenReturn("MrScheduleTel.scheduleId");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.nationName"))
        .thenReturn("MrScheduleTel.nationName");
//    PowerMockito.when(I18n.getLanguage("MrScheduleTel.region"))
//        .thenReturn("MrScheduleTel.region");
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
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.crId"))
        .thenReturn("MrScheduleTel.crId");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.implementUnitName"))
        .thenReturn("MrScheduleTel.implementUnitName");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.checkingUnitName"))
        .thenReturn("MrScheduleTel.checkingUnitName");
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
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.groupCode"))
        .thenReturn("MrScheduleTel.groupCode");
    PowerMockito.when(I18n.getLanguage("MrScheduleTel.impactNode"))
        .thenReturn("MrScheduleTel.impactNode");
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

    Object[] objectHeader = new Object[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("MrScheduleTel.scheduleId"),
        I18n.getLanguage("MrScheduleTel.nationName"),
//        I18n.getLanguage("MrScheduleTel.region"),
        I18n.getLanguage("MrScheduleTel.arrayCode"),
        I18n.getLanguage("MrScheduleTel.networkType"),
        I18n.getLanguage("MrScheduleTel.deviceType"),
        I18n.getLanguage("MrScheduleTel.deviceCode"),
        I18n.getLanguage("MrScheduleTel.deviceName"),
        I18n.getLanguage("MrScheduleTel.nodeIp"),
        I18n.getLanguage("MrScheduleTel.vendor"),
        I18n.getLanguage("MrScheduleTel.mrId"),//11
        I18n.getLanguage("MrScheduleTel.crId"),
        I18n.getLanguage("MrScheduleTel.implementUnitName"),
        I18n.getLanguage("MrScheduleTel.checkingUnitName"),
        I18n.getLanguage("MrScheduleTel.cycle"),
        I18n.getLanguage("MrScheduleTel.cycleTypeName"),
        I18n.getLanguage("MrScheduleTel.woContent"),
        I18n.getLanguage("MrScheduleTel.lastDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateModifyStr"),
        I18n.getLanguage("MrScheduleTel.updatedDateStr"),
        I18n.getLanguage("MrScheduleTel.groupCode"),
        I18n.getLanguage("MrScheduleTel.impactNode"),
        I18n.getLanguage("MrScheduleTel.scheduleType"),
        I18n.getLanguage("MrScheduleTel.station"),
        I18n.getLanguage("MrScheduleTel.mrConfirmName"),
        I18n.getLanguage("MrScheduleTel.mrComment")
    };
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    headerList.add(objectHeader);
    PowerMockito
        .when(CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            7,
            0,
            26,
            1000
        ))
        .thenReturn(headerList);
    //</editor-fold>
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    when(FileUtils
        .saveTempFile(any(), any(), anyString())).thenReturn(filePath);
    when(mrScheduleTelRepository
        .getListMrSheduleTelByIdForImport(any())).thenReturn(mrScheduleTelDTOArrayList);
    PowerMockito.when(I18n.getLanguage("mrScheduleTel.networkType.viba"))
        .thenReturn("Link/Tuyến viba");
    when(mrScheduleTelRepository.updateMrScheduleTel(any())).thenReturn(res);
    when(mrDeviceRepository.findMrDeviceById(any())).thenReturn(mrDeviceDTO);
    mrScheduleTelBusiness.importSoftData(multipartFile);
  }

  @Test
  public void updateMrconfirmForImport() {
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(2L);
    mrScheduleTelDTO.setCrId(2L);

    MrScheduleTelDTO mrScheduleTelOldDTO = Mockito.spy(MrScheduleTelDTO.class);

    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    List<MrInsideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrInsideDTO);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    List<CrHisDTO> lstCr = Mockito.spy(ArrayList.class);
    lstCr.add(crHisDTO);

    when(mrScheduleTelRepository.checkExistCrId(null, null)).thenReturn(lstCr);
    when(mrScheduleTelRepository.findById(any())).thenReturn(mrScheduleTelDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Mã CR không đúng hoặc CR không đủ điều kiện");
    mrScheduleTelBusiness.updateMrconfirmForImport(mrScheduleTelDTO, mrScheduleTelOldDTO);
  }

  @Test
  public void updateMrconfirmForImport1() {
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(3L);
    mrScheduleTelDTO.setCrId(3L);
    mrScheduleTelDTO.setMrId(2L);

    MrScheduleTelDTO mrScheduleTelOldDTO = Mockito.spy(MrScheduleTelDTO.class);

    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    List<MrInsideDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrInsideDTO);

    MrCfgProcedureTelDTO dtoNew = Mockito.spy(MrCfgProcedureTelDTO.class);

    ResultInSideDto objResultHis = Mockito.spy(ResultInSideDto.class);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    crHisDTO.setEarliestStartTime("07/04/2020");
    crHisDTO.setLatestEndTime("07/04/2020");
    List<CrHisDTO> lstCr = Mockito.spy(ArrayList.class);
    lstCr.add(crHisDTO);
    when(mrScheduleTelRepository.checkExistCrId("3", null)).thenReturn(lstCr);
    when(mrScheduleTelRepository.findById(any())).thenReturn(mrScheduleTelDTO);
    when(mrCfgProcedureTelRepository.findMrCfgProcedureTelById(any())).thenReturn(dtoNew);
    when(mrScheduleTelHisRepository.insertScheduleHis(any())).thenReturn(objResultHis);
    when(mrRepository.findMrById(any())).thenReturn(mrInsideDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(any()))
        .thenReturn("Move lịch do cập nhật lý do bảo dưỡng = Nâng cấp");
    mrScheduleTelBusiness.updateMrconfirmForImport(mrScheduleTelDTO, mrScheduleTelOldDTO);
  }

  @Test
  public void updateMrconfirmForImport2() {
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setCrId(null);
    MrScheduleTelDTO mrScheduleTelOldDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelBusiness.updateMrconfirmForImport(mrScheduleTelDTO, mrScheduleTelOldDTO);
  }

  @Test
  public void updateMrconfirm() {
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(3L);
    mrScheduleTelDTO.setCrId(3L);
    mrScheduleTelDTO.setMrId(2L);

    MrScheduleTelDTO mrScheduleTelOldDTO = Mockito.spy(MrScheduleTelDTO.class);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    List<CrHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(crHisDTO);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");

    when(mrScheduleTelRepository.checkExistCrId(null, null)).thenReturn(lst);
    when(mrScheduleTelRepository.findById(any())).thenReturn(mrScheduleTelDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Mã CR không đúng hoặc CR không đủ điều kiện");
    mrScheduleTelBusiness.updateMrconfirm(mrScheduleTelDTO, mrScheduleTelOldDTO, userToken);
  }

  @Test
  public void updateMrconfirm1() {
    PowerMockito.mockStatic(I18n.class);

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(2L);
    mrScheduleTelDTO.setCrId(3L);
    mrScheduleTelDTO.setMarketCode("281");
    mrScheduleTelDTO.setArrayCode("12");
    mrScheduleTelDTO.setDeviceType("MPD");
    mrScheduleTelDTO.setDeviceId(2L);
    mrScheduleTelDTO.setDeviceCode("POIS");
    mrScheduleTelDTO.setNetworkType("DOST");
    mrScheduleTelDTO.setDeviceName("DieuHoa");
    mrScheduleTelDTO.setMrId(2L);
    mrScheduleTelDTO.setMrContentId("test");
    mrScheduleTelDTO.setMrMode("MrMode");
    mrScheduleTelDTO.setMrType("MrType");
    mrScheduleTelDTO.setMrId(2L);
    mrScheduleTelDTO.setMrCode("mrCode");
    mrScheduleTelDTO.setCrNumber("34");
    mrScheduleTelDTO.setProcedureId(3L);
    mrScheduleTelDTO.setProcedureName("Proces");
    mrScheduleTelDTO.setMrSoft("1");
    PowerMockito.when(I18n.getValidation("mrScheduleTelhis.note.mrConfirmEqualFive"))
        .thenReturn("Move lịch do cập nhật lý do bảo dưỡng = Nâng cấp");
    mrScheduleTelDTO.setRegion("KV1");
    mrScheduleTelDTO.setCycle("3");

    MrScheduleTelDTO mrScheduleTelOldDTO = Mockito.spy(MrScheduleTelDTO.class);

    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);

    MrDeviceDTO dtoMD = Mockito.spy(MrDeviceDTO.class);

    ResultInSideDto objResultHis = Mockito.spy(ResultInSideDto.class);
    objResultHis.setKey(RESULT.SUCCESS);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    crHisDTO.setEarliestStartTime("07/04/2020");
    crHisDTO.setLatestEndTime("07/04/2020");
    List<CrHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(crHisDTO);
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");

    MrCfgProcedureTelDTO dtoNew = Mockito.spy(MrCfgProcedureTelDTO.class);

    when(mrScheduleTelRepository.checkExistCrId("3", null)).thenReturn(lst);
    when(mrScheduleTelRepository.findById(any())).thenReturn(mrScheduleTelDTO);
    when(mrCfgProcedureTelRepository.findMrCfgProcedureTelById(any())).thenReturn(dtoNew);
    when(mrRepository.findMrById(any())).thenReturn(mrInsideDTO);
    when(mrScheduleTelHisRepository.insertScheduleHis(any())).thenReturn(objResultHis);
    when(mrDeviceRepository.findMrDeviceById(any())).thenReturn(dtoMD);
    mrScheduleTelBusiness.updateMrconfirm(mrScheduleTelDTO, mrScheduleTelOldDTO, userToken);
  }

  @Test
  public void updateMrconfirm2() {
    PowerMockito.mockStatic(I18n.class);

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(2L);
    mrScheduleTelDTO.setCrId(null);
    mrScheduleTelDTO.setMrConfirm("6");
    mrScheduleTelDTO.setMrSoft("1");

    MrScheduleTelDTO mrScheduleTelOldDTO = Mockito.spy(MrScheduleTelDTO.class);

    MrDeviceDTO dtoMD = Mockito.spy(MrDeviceDTO.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");

    when(mrScheduleTelRepository.findById(any())).thenReturn(mrScheduleTelDTO);
    when(mrDeviceRepository.findMrDeviceById(any())).thenReturn(dtoMD);
    mrScheduleTelBusiness.updateMrconfirm(mrScheduleTelDTO, mrScheduleTelOldDTO, userToken);
  }

  @Test
  public void updateMrconfirm3() {
    PowerMockito.mockStatic(I18n.class);

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(2L);
    mrScheduleTelDTO.setCrId(null);
    mrScheduleTelDTO.setMrConfirm("7");
    mrScheduleTelDTO.setMrSoft("2");

    MrScheduleTelDTO mrScheduleTelOldDTO = Mockito.spy(MrScheduleTelDTO.class);

    MrDeviceDTO dtoMD = Mockito.spy(MrDeviceDTO.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");

    when(mrScheduleTelRepository.findById(any())).thenReturn(mrScheduleTelDTO);
    when(mrDeviceRepository.findMrDeviceById(any())).thenReturn(dtoMD);
    mrScheduleTelBusiness.updateMrconfirm(mrScheduleTelDTO, mrScheduleTelOldDTO, userToken);
  }

  @Test
  public void processDataImport() {
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 3; i++) {
      Object[] objectTest = new Object[30];
      for (int j = 0; j < 30; j++) {
        if (j == 19) {
          objectTest[j] = "29/03/2021";
        } else {
          objectTest[j] = "2";
        }
      }
      dataImportList.add(objectTest);
    }

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);

    List<MrScheduleTelDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleTelDTO);
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Bản ghi không tồn tại ");
    String type = "S";
    mrScheduleTelBusiness.processDataImport(dataImportList, lst, type);
  }

  @Test
  public void getMapFromList() {
    String st = "2";
    List<String> lst = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 3; i++) {
      lst.add(st);
    }
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(2L);
    List<MrScheduleTelDTO> results = Mockito.spy(ArrayList.class);
    results.add(mrScheduleTelDTO);
    when(mrScheduleTelRepository
        .getListMrSheduleTelByIdForImport(any())).thenReturn(results);
    mrScheduleTelBusiness.getMapFromList(lst);
  }

  @Test
  public void readerHeaderSheet() {
    String col = "test";
    mrScheduleTelBusiness.readerHeaderSheet(col);
  }

  @Test
  public void validateFileSoftFormat() {
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] objectTest = new Object[]{};
    for (int i = 0; i < 4; i++) {
      headerList.add(objectTest);
    }
    String[] headerConfig = {"a", "b", "c"};
    mrScheduleTelBusiness.validateFileSoftFormat(headerList, headerConfig);
  }

  @Test
  public void validateFileSoftFormat0() {
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] objectTest = null;
    for (int i = 0; i < 7; i++) {
      headerList.add(objectTest);
    }
    String[] headerConfig = {"a", "b", "c"};
    mrScheduleTelBusiness.validateFileSoftFormat(headerList, headerConfig);
  }

  @Test
  public void validateFileSoftFormat1() {
    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    Object[] objectTest = new Object[2];
    objectTest[0] = "2";
    objectTest[1] = "3";
    headerList.add(objectTest);
    String[] headerConfig = {"2", "3"};
    mrScheduleTelBusiness.validateFileSoftFormat(headerList, headerConfig);
  }

  @Test
  public void getHeaderSoft() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Thị trường ");
    mrScheduleTelBusiness.getHeaderSoft();
  }

  @Test
  public void onUpdateSoft() {
    MrScheduleTelDTO dtoOld = Mockito.spy(MrScheduleTelDTO.class);
    dtoOld.setScheduleId(2L);
    dtoOld.setDeviceId(2L);

    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setScheduleId(2L);
    mrScheduleTelDTO.setMrId(2L);
    mrScheduleTelDTO.setCrId(3L);
    mrScheduleTelDTO.setMrConfirm("1");
    mrScheduleTelDTO.setNextDateModify(DateTimeUtils.convertStringToDate("27/03/2021 12:12:12"));

    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");

    CrInsiteDTO form = Mockito.spy(CrInsiteDTO.class);
    form.setCreatedDate(new Date());
    form.setEarliestStartTime(new Date());

    MrDeviceDTO deviceUpdate = Mockito.spy(MrDeviceDTO.class);
    deviceUpdate.setDeviceId(2L);

    CrImpactedNodesDTO crImpactedNodesDTO = Mockito.spy(CrImpactedNodesDTO.class);
    List<CrImpactedNodesDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(crImpactedNodesDTO);

    MrScheduleTelDTO objUpdate = Mockito.spy(MrScheduleTelDTO.class);
    objUpdate.setMrId(3L);

    when(mrScheduleTelBusiness.getDetail(any(), any())).thenReturn(dtoOld);
    when(crServiceProxy.findCrByIdProxy(any())).thenReturn(form);
    when(crServiceProxy.getLisNodeOfCRForProxy(any())).thenReturn(lst);
    when(mrDeviceRepository.findMrDeviceById(anyLong())).thenReturn(deviceUpdate);
    when(mrScheduleTelRepository.findById(any())).thenReturn(objUpdate);
    List<MrConfigDTO> lstMrConfigDTO = Mockito.spy(ArrayList.class);
    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigCode("1");
    mrConfigDTO.setConfigValue("UPDATE_MR");
    lstMrConfigDTO.add(mrConfigDTO);
    when(mrScheduleTelRepository.getConfigByGroup("LY_DO_KO_BD")).thenReturn(lstMrConfigDTO);
    MrInsideDTO mrInsideDTO = Mockito.spy(MrInsideDTO.class);
    when(mrScheduleTelRepository.findMrById(anyLong())).thenReturn(mrInsideDTO);
    mrScheduleTelBusiness.onUpdateSoft(mrScheduleTelDTO);

    mrScheduleTelDTO.setMrId(null);
    mrScheduleTelBusiness.onUpdateSoft(mrScheduleTelDTO);

    mrScheduleTelDTO.setCrId(null);
    mrScheduleTelBusiness.onUpdateSoft(mrScheduleTelDTO);

    MrScheduleTelDTO objUpdate2 = Mockito.spy(MrScheduleTelDTO.class);
    objUpdate2.setGroupCode("1");
    when(mrScheduleTelRepository.findById(anyLong())).thenReturn(objUpdate2);

    List<MrScheduleTelDTO> lstUpdate = Mockito.spy(ArrayList.class);
    lstUpdate.add(objUpdate2);
    when(mrScheduleTelRepository.getDataExport(any(), anyString())).thenReturn(lstUpdate);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    when(mrScheduleTelRepository.updateMrScheduleTel(any())).thenReturn(resultInSideDto);

    mrScheduleTelDTO.setMrConfirm(null);
    mrScheduleTelBusiness.onUpdateSoft(mrScheduleTelDTO);
  }

  @Test
  public void test_deleteMrScheduleTelSoft() {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getValidation(any()))
        .thenReturn("Xóa không thành công bản ghi chọn có chứa Mr/Cr");
    List<MrScheduleTelDTO> mrScheduleTelDTOS = Mockito.spy(ArrayList.class);
    MrScheduleTelDTO mrScheduleTelDTO = Mockito.spy(MrScheduleTelDTO.class);
    mrScheduleTelDTO.setDeviceCode("1");
    mrScheduleTelDTO.setDeviceId(1L);
    mrScheduleTelDTOS.add(mrScheduleTelDTO);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.ERROR);
    when(mrScheduleTelRepository.deleteListSchedule(any())).thenReturn(resultInSideDto);
    List<MrScheduleTelDTO> lstSchedule = Mockito.spy(ArrayList.class);
    lstSchedule.add(mrScheduleTelDTO);
    when(mrScheduleTelRepository.getDataExport(any(), anyString())).thenReturn(lstSchedule);
    MrDeviceDTO dtoMD = Mockito.spy(MrDeviceDTO.class);
    when(mrDeviceRepository.findMrDeviceById(anyLong())).thenReturn(dtoMD);
    mrScheduleTelBusiness.deleteMrScheduleTelSoft(mrScheduleTelDTOS);

    mrScheduleTelDTOS.get(0).setCrId(1L);
    mrScheduleTelDTOS.get(0).setMrId(1L);
    ResultInSideDto result = mrScheduleTelBusiness.deleteMrScheduleTelSoft(mrScheduleTelDTOS);
    Assert.assertEquals(result.getKey(), resultInSideDto.getKey());
  }

  @Test
  public void test_findWoById() {
    PowerMockito.mockStatic(TicketProvider.class);
    UserToken userToken = Mockito.spy(UserToken.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");

    MrInsideDTO mrDTO = Mockito.spy(MrInsideDTO.class);
    mrDTO.setMrId(1L);
    when(mrServiceRepository.findMrById(anyLong())).thenReturn(mrDTO);

    WoInsideDTO woInsideDTO = Mockito.spy(WoInsideDTO.class);
    woInsideDTO.setWoId(1L);
    woInsideDTO.setWoTypeId(1L);
    when(woServiceProxy.findWoByIdProxy(anyLong())).thenReturn(woInsideDTO);

    List<WoDTOSearch> lstTemp = Mockito.spy(ArrayList.class);
    WoDTOSearch woDTOSearch = Mockito.spy(WoDTOSearch.class);
    lstTemp.add(woDTOSearch);
    when(woServiceProxy.getListDataSearchProxy(any())).thenReturn(lstTemp);

    List<WoTypeInsideDTO> listWoTypeDTO = Mockito.spy(ArrayList.class);
    WoTypeInsideDTO woTypeInsideDTO = Mockito.spy(WoTypeInsideDTO.class);
    woTypeInsideDTO.setWoTypeId(1L);
    listWoTypeDTO.add(woTypeInsideDTO);
    when(woCategoryServiceProxy.getListWoTypeDTO(any())).thenReturn(listWoTypeDTO);

    mrScheduleTelBusiness.findWoById(1L, 1L);

    mrDTO.setState("4");
    when(mrServiceRepository.findMrById(anyLong())).thenReturn(mrDTO);

    mrScheduleTelBusiness.findWoById(1L, 1L);
  }
}
