package com.viettel.gnoc.business;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.LogChangeConfigBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.ShiftHandoverDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.TtServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ShiftCrDTO;
import com.viettel.gnoc.cr.dto.ShiftItDTO;
import com.viettel.gnoc.cr.dto.ShiftItSeriousDTO;
import com.viettel.gnoc.cr.dto.ShiftStaftDTO;
import com.viettel.gnoc.cr.dto.ShiftWorkDTO;
import com.viettel.gnoc.cr.dto.ShiftWorkOtherDTO;
import com.viettel.gnoc.cr.model.ShiftHandoverFileEntity;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.repository.ShiftCrRepository;
import com.viettel.gnoc.repository.ShiftHandoverRepository;
import com.viettel.gnoc.repository.ShiftItRepository;
import com.viettel.gnoc.repository.ShiftItSeriousRepository;
import com.viettel.gnoc.repository.ShiftStaftRepository;
import com.viettel.gnoc.repository.ShiftWorkOtherRepository;
import com.viettel.gnoc.repository.ShiftWorkRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ShiftHandoverBusinessImplTest.class, FileUtils.class, CommonImport.class,
    I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class ShiftHandoverBusinessImplTest {

  @InjectMocks
  ShiftHandoverBusinessImpl shiftHandoverBusiness;

  @Mock
  ShiftHandoverRepository shiftHandoverRepository;

  @Mock
  ShiftStaftRepository shiftStaftRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  UnitRepository unitRepository;

  @Mock
  ShiftWorkRepository shiftWorkRepository;

  @Mock
  ShiftItSeriousRepository shiftItSeriousRepository;

  @Mock
  ShiftItRepository shiftItRepository;

  @Mock
  LogChangeConfigBusiness logChangeConfigBusiness;

  @Mock
  ShiftCrRepository shiftCrRepository;

  @Mock
  ShiftWorkOtherRepository shiftWorkOtherRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  CrServiceProxy crServiceProxy;

  @Mock
  CatItemBusiness catItemBusiness;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  TtServiceProxy ttServiceProxy;

  @Test
  public void findShiftHandoverById() {
    Long id = 1L;
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    when(shiftHandoverRepository.findShiftHandoverById(any())).thenReturn(shiftHandoverDTO);
    ShiftHandoverDTO result = shiftHandoverBusiness.findShiftHandoverById(id);
    assertEquals(shiftHandoverDTO, result);
  }

  @Test
  public void getListShiftHandover() {
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    List<ShiftHandoverDTO> list = Mockito.spy(ArrayList.class);
    list.add(shiftHandoverDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(list);
    when(shiftHandoverRepository.getListShiftHandover(any())).thenReturn(datatable);

    List<ShiftStaftDTO> dtoList = Mockito.spy(ArrayList.class);
    when(shiftStaftRepository.getListShiftStaftById(any())).thenReturn(dtoList);

    shiftHandoverBusiness.getListShiftHandover(shiftHandoverDTO);
  }

  @Test
  public void exportData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    ShiftHandoverDTO dto = Mockito.spy(ShiftHandoverDTO.class);
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    shiftHandoverBusiness.exportData(dto);
  }

  @Test
  public void exportShiftWorkData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    ShiftWorkDTO shiftWorkDTO = Mockito.spy(ShiftWorkDTO.class);
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    shiftHandoverBusiness.exportShiftWorkData(shiftWorkDTO);
  }

  @Test
  public void exportShiftCrData1() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    ShiftCrDTO shiftCrDTO = Mockito.spy(ShiftCrDTO.class);
    File fileExportSuccess = new File("./test_junit/test.txt");
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    shiftHandoverBusiness.exportShiftCrData(shiftCrDTO);
  }

  @Test
  public void getListShiftID() {
    List<ShiftHandoverDTO> list = Mockito.spy(ArrayList.class);
    when(shiftHandoverRepository.getListShiftID()).thenReturn(list);
    List<ShiftHandoverDTO> resultList = shiftHandoverBusiness.getListShiftID();
    assertEquals(list.size(), resultList.size());
  }

  @Test
  public void insertOrUpdateCfgShiftHandoverFile() {
  }

  @Test
  public void insertCfgShiftHandover() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unitToken);

    boolean check = false;
    when(shiftHandoverRepository.checkDuplicateRecord(any())).thenReturn(check);

    shiftHandoverBusiness.insertCfgShiftHandover(files, shiftHandoverDTO);
  }

  @Test
  public void insertCfgShiftHandover1() throws Exception {
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    files.add(multipartFile);

    ShiftStaftDTO shiftStaftDTO = Mockito.spy(ShiftStaftDTO.class);
    List<ShiftStaftDTO> lstStart = Mockito.spy(ArrayList.class);
    lstStart.add(shiftStaftDTO);

    ShiftWorkDTO shiftWorkDTO = Mockito.spy(ShiftWorkDTO.class);
    List<ShiftWorkDTO> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(shiftWorkDTO);

    ShiftItSeriousDTO shiftItSeriousDTO = Mockito.spy(ShiftItSeriousDTO.class);
    List<ShiftItSeriousDTO> lstSerious = Mockito.spy(ArrayList.class);
    lstSerious.add(shiftItSeriousDTO);

    ShiftItDTO shiftItDTO = Mockito.spy(ShiftItDTO.class);
    List<ShiftItDTO> lstShift = Mockito.spy(ArrayList.class);
    lstShift.add(shiftItDTO);

    ShiftCrDTO shiftCrDTO = Mockito.spy(ShiftCrDTO.class);
    List<ShiftCrDTO> lstCr = Mockito.spy(ArrayList.class);
    lstCr.add(shiftCrDTO);

    ShiftWorkOtherDTO shiftWorkOtherDTO = Mockito.spy(ShiftWorkOtherDTO.class);
    List<ShiftWorkOtherDTO> lstWorkOther = Mockito.spy(ArrayList.class);
    lstWorkOther.add(shiftWorkOtherDTO);

    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setShiftStaftDTOList(lstStart);
    shiftHandoverDTO.setShiftWorkDTOList(lstWo);
    shiftHandoverDTO.setShiftItSeriousDTOList(lstSerious);
    shiftHandoverDTO.setShiftItDTOList(lstShift);
    shiftHandoverDTO.setShiftCrDTOList(lstCr);
    shiftHandoverDTO.setShiftWorkOtherDTOList(lstWorkOther);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unitToken);

    boolean check = true;
    when(shiftHandoverRepository.checkDuplicateRecord(any())).thenReturn(check);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("FALSE");
    when(shiftHandoverRepository.insertOrUpdateCfgShiftHandover(any())).thenReturn(resultInSideDto);
    when(shiftWorkRepository.insertShiftWork(any())).thenReturn(resultInSideDto);
    when(shiftItSeriousRepository.insertShiftItSerious(any())).thenReturn(resultInSideDto);
    when(shiftItRepository.insertShiftIt(any())).thenReturn(resultInSideDto);
    when(shiftCrRepository.insertShiftCr(any())).thenReturn(resultInSideDto);
    when(shiftWorkOtherRepository.insertShiftWorkOther(any())).thenReturn(resultInSideDto);
    when(logChangeConfigBusiness
        .insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "InsertCfgShiftHandover", "Add InsertCfgShiftHandover ID: " + "test",
            shiftHandoverDTO, null))).thenReturn(resultInSideDto);

    shiftHandoverBusiness.insertCfgShiftHandover(files, shiftHandoverDTO);
  }

  @Test
  public void insertCfgShiftHandover2() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    files.add(multipartFile);

    ShiftStaftDTO shiftStaftDTO = Mockito.spy(ShiftStaftDTO.class);
    shiftStaftDTO.setAssignUserId(1L);
    shiftStaftDTO.setReceiveUserId(1L);
    List<ShiftStaftDTO> lstStart = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 2; i++) {
      lstStart.add(shiftStaftDTO);
    }

    ShiftWorkDTO shiftWorkDTO = Mockito.spy(ShiftWorkDTO.class);
    List<ShiftWorkDTO> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(shiftWorkDTO);

    ShiftItSeriousDTO shiftItSeriousDTO = Mockito.spy(ShiftItSeriousDTO.class);
    List<ShiftItSeriousDTO> lstSerious = Mockito.spy(ArrayList.class);
    lstSerious.add(shiftItSeriousDTO);

    ShiftItDTO shiftItDTO = Mockito.spy(ShiftItDTO.class);
    List<ShiftItDTO> lstShift = Mockito.spy(ArrayList.class);
    lstShift.add(shiftItDTO);

    ShiftCrDTO shiftCrDTO = Mockito.spy(ShiftCrDTO.class);
    List<ShiftCrDTO> lstCr = Mockito.spy(ArrayList.class);
    lstCr.add(shiftCrDTO);

    ShiftWorkOtherDTO shiftWorkOtherDTO = Mockito.spy(ShiftWorkOtherDTO.class);
    List<ShiftWorkOtherDTO> lstWorkOther = Mockito.spy(ArrayList.class);
    lstWorkOther.add(shiftWorkOtherDTO);

    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setShiftStaftDTOList(lstStart);
    shiftHandoverDTO.setShiftWorkDTOList(lstWo);
    shiftHandoverDTO.setShiftItSeriousDTOList(lstSerious);
    shiftHandoverDTO.setShiftItDTOList(lstShift);
    shiftHandoverDTO.setShiftCrDTOList(lstCr);
    shiftHandoverDTO.setShiftWorkOtherDTOList(lstWorkOther);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    PowerMockito.when(unitRepository.findUnitById(any())).thenReturn(unitToken);

    boolean check = false;
    when(shiftHandoverRepository.checkDuplicateRecord(any())).thenReturn(check);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("FALSE");
    when(shiftHandoverRepository.insertOrUpdateCfgShiftHandover(any())).thenReturn(resultInSideDto);
    when(shiftWorkRepository.insertShiftWork(any())).thenReturn(resultInSideDto);
    when(shiftItSeriousRepository.insertShiftItSerious(any())).thenReturn(resultInSideDto);
    when(shiftItRepository.insertShiftIt(any())).thenReturn(resultInSideDto);
    when(shiftCrRepository.insertShiftCr(any())).thenReturn(resultInSideDto);
    when(shiftWorkOtherRepository.insertShiftWorkOther(any())).thenReturn(resultInSideDto);
    when(logChangeConfigBusiness
        .insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "InsertCfgShiftHandovers", "Add InsertCfgShiftHandover ID: " + "test",
            shiftHandoverDTO, null))).thenReturn(resultInSideDto);

    shiftHandoverBusiness.insertCfgShiftHandover(files, shiftHandoverDTO);
  }


  @Test
  public void updateCfgShiftHandover() throws Exception {
    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    PowerMockito.mockStatic(I18n.class);
    ShiftStaftDTO shiftStaftDTO = Mockito.spy(ShiftStaftDTO.class);
    shiftStaftDTO.setIsDeleteShiftStaft(true);
    List<ShiftStaftDTO> lstStaft = Mockito.spy(ArrayList.class);
    lstStaft.add(shiftStaftDTO);
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(DateTimeUtils.convertStringToDate("27/05/2020 12:12:12"));
    shiftHandoverDTO.setShiftStaftDTOList(lstStaft);

    shiftHandoverBusiness.updateCfgShiftHandover(files, shiftHandoverDTO);
  }

  @Test
  public void updateCfgShiftHandover1() throws Exception {
    PowerMockito.mockStatic(I18n.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    List<MultipartFile> files = Mockito.spy(ArrayList.class);

    ShiftStaftDTO shiftStaftDTO = Mockito.spy(ShiftStaftDTO.class);
    shiftStaftDTO.setIsDeleteShiftStaft(false);
    List<ShiftStaftDTO> lstStaft = Mockito.spy(ArrayList.class);
    lstStaft.add(shiftStaftDTO);

    UsersEntity userAssign = Mockito.spy(UsersEntity.class);
    userAssign.setUnitId(2L);
    when(userRepository.getUserByUserId(any())).thenReturn(userAssign);

    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setOpenToolTip(1L);
    shiftHandoverDTO.setShiftStaftDTOList(lstStaft);

    shiftHandoverBusiness.updateCfgShiftHandover(files, shiftHandoverDTO);
  }

  @Test
  public void updateCfgShiftHandover2() throws Exception {
    PowerMockito.mockStatic(I18n.class);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    List<MultipartFile> files = Mockito.spy(ArrayList.class);

    ShiftStaftDTO shiftStaftDTO = Mockito.spy(ShiftStaftDTO.class);
    shiftStaftDTO.setIsDeleteShiftStaft(true);
    List<ShiftStaftDTO> lstStafts = Mockito.spy(ArrayList.class);
    lstStafts.add(shiftStaftDTO);

    UsersEntity userAssign = Mockito.spy(UsersEntity.class);
    userAssign.setUnitId(2L);
    when(userRepository.getUserByUserId(any())).thenReturn(userAssign);

    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setOpenToolTip(null);
    shiftHandoverDTO.setShiftStaftDTOList(lstStafts);

    shiftHandoverBusiness.updateCfgShiftHandover(files, shiftHandoverDTO);
  }

  @Test
  public void updateCfgShiftHandover3() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("FALSE");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    files.add(multipartFile);

    ShiftStaftDTO shiftStaftDTO = Mockito.spy(ShiftStaftDTO.class);
    shiftStaftDTO.setIsDeleteShiftStaft(true);
    shiftStaftDTO.setId(2L);
    List<ShiftStaftDTO> lstStafts = Mockito.spy(ArrayList.class);
    lstStafts.add(shiftStaftDTO);

    ShiftCrDTO shiftCrDTO = Mockito.spy(ShiftCrDTO.class);
    shiftCrDTO.setIsDeleteShiftCr(true);
    shiftCrDTO.setId(2L);
    List<ShiftCrDTO> lstCr = Mockito.spy(ArrayList.class);
    lstCr.add(shiftCrDTO);

    ShiftWorkDTO shiftWorkDTO = Mockito.spy(ShiftWorkDTO.class);
    shiftWorkDTO.setIsDeleteShiftWork(true);
    shiftWorkDTO.setId(2L);
    List<ShiftWorkDTO> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(shiftWorkDTO);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setMappingId(1L);
    List<GnocFileDto> gnocFileDtosWeb = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 3; i++) {
      gnocFileDtosWeb.add(gnocFileDto);
    }
    UsersEntity userAssign = Mockito.spy(UsersEntity.class);
    userAssign.setUnitId(2L);
    when(userRepository.getUserByUserId(any())).thenReturn(userAssign);

    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setOpenToolTip(null);
    shiftHandoverDTO.setShiftStaftDTOList(lstStafts);
    shiftHandoverDTO.setShiftWorkDTOList(lstWo);
    shiftHandoverDTO.setShiftCrDTOList(lstCr);
    shiftHandoverDTO.setCreatedTimeOld(new Date());
    shiftHandoverDTO.setShiftCrDTOList(lstCr);
    shiftHandoverDTO.setGnocFileDtos(gnocFileDtosWeb);

    when(shiftHandoverRepository.updateCfgShiftHandover(any())).thenReturn(resultInSideDto);
    when(shiftStaftRepository.deleteShiftUser(any())).thenReturn(resultInSideDto);
    when(shiftWorkRepository.deleteShiftWork(any())).thenReturn(resultInSideDto);
    when(shiftCrRepository.deleteShiftCr(any())).thenReturn(resultInSideDto);
    when(logChangeConfigBusiness
        .insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "UpdateCfgShiftHandover", "UpdateCfgShiftHandover ID: " + shiftHandoverDTO.getId(),
            shiftHandoverDTO, null))).thenReturn(resultInSideDto);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    when(unitRepository.findUnitById(any())).thenReturn(unitToken);

    ShiftHandoverFileEntity shiftHandoverFileEntity = Mockito.spy(ShiftHandoverFileEntity.class);
    List<ShiftHandoverFileEntity> lstFileOld = Mockito.spy(ArrayList.class);
    lstFileOld.add(shiftHandoverFileEntity);
    when(shiftHandoverRepository.findShiftHandOverFile(any())).thenReturn(lstFileOld);

    shiftHandoverBusiness.updateCfgShiftHandover(files, shiftHandoverDTO);
  }

  @Test
  public void updateCfgShiftHandover4() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("FALSE");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    files.add(multipartFile);

    ShiftStaftDTO shiftStaftDTO = Mockito.spy(ShiftStaftDTO.class);
    shiftStaftDTO.setIsDeleteShiftStaft(false);
    shiftStaftDTO.setId(3L);
    List<ShiftStaftDTO> lstStafts = Mockito.spy(ArrayList.class);
    lstStafts.add(shiftStaftDTO);

    ShiftCrDTO shiftCrDTO = Mockito.spy(ShiftCrDTO.class);
    shiftCrDTO.setIsDeleteShiftCr(false);
    shiftCrDTO.setId(2L);
    List<ShiftCrDTO> lstCr = Mockito.spy(ArrayList.class);
    lstCr.add(shiftCrDTO);

    ShiftWorkDTO shiftWorkDTO = Mockito.spy(ShiftWorkDTO.class);
    shiftWorkDTO.setIsDeleteShiftWork(false);
    shiftWorkDTO.setId(2L);
    List<ShiftWorkDTO> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(shiftWorkDTO);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setMappingId(1L);
    List<GnocFileDto> gnocFileDtosWeb = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 3; i++) {
      gnocFileDtosWeb.add(gnocFileDto);
    }
    UsersEntity userAssign = Mockito.spy(UsersEntity.class);
    userAssign.setUnitId(2L);
    when(userRepository.getUserByUserId(any())).thenReturn(userAssign);

    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setOpenToolTip(null);
    shiftHandoverDTO.setShiftStaftDTOList(lstStafts);
    shiftHandoverDTO.setShiftWorkDTOList(lstWo);
    shiftHandoverDTO.setShiftCrDTOList(lstCr);
    shiftHandoverDTO.setCreatedTimeOld(new Date());
    shiftHandoverDTO.setShiftCrDTOList(lstCr);
    shiftHandoverDTO.setGnocFileDtos(gnocFileDtosWeb);

    when(shiftHandoverRepository.updateCfgShiftHandover(any())).thenReturn(resultInSideDto);
    when(shiftStaftRepository.updateShiftUser(any())).thenReturn(resultInSideDto);
    when(shiftWorkRepository.updateShiftWork(any())).thenReturn(resultInSideDto);
    when(shiftCrRepository.updateShiftCr(any())).thenReturn(resultInSideDto);
    when(logChangeConfigBusiness
        .insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "UpdateCfgShiftHandover", "UpdateCfgShiftHandover ID: " + shiftHandoverDTO.getId(),
            shiftHandoverDTO, null))).thenReturn(resultInSideDto);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    when(unitRepository.findUnitById(any())).thenReturn(unitToken);

    ShiftHandoverFileEntity shiftHandoverFileEntity = Mockito.spy(ShiftHandoverFileEntity.class);
    List<ShiftHandoverFileEntity> lstFileOld = Mockito.spy(ArrayList.class);
    lstFileOld.add(shiftHandoverFileEntity);
    when(shiftHandoverRepository.findShiftHandOverFile(any())).thenReturn(lstFileOld);

    shiftHandoverBusiness.updateCfgShiftHandover(files, shiftHandoverDTO);
  }

  @Test
  public void updateCfgShiftHandover5() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("FALSE");

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    List<MultipartFile> files = Mockito.spy(ArrayList.class);
    files.add(multipartFile);

    ShiftStaftDTO shiftStaftDTO = Mockito.spy(ShiftStaftDTO.class);
    shiftStaftDTO.setIsDeleteShiftStaft(true);
    shiftStaftDTO.setId(null);
    List<ShiftStaftDTO> lstStafts = Mockito.spy(ArrayList.class);
    lstStafts.add(shiftStaftDTO);

    ShiftCrDTO shiftCrDTO = Mockito.spy(ShiftCrDTO.class);
    shiftCrDTO.setIsDeleteShiftCr(false);
    shiftCrDTO.setId(null);
    List<ShiftCrDTO> lstCr = Mockito.spy(ArrayList.class);
    lstCr.add(shiftCrDTO);

    ShiftWorkDTO shiftWorkDTO = Mockito.spy(ShiftWorkDTO.class);
    shiftWorkDTO.setIsDeleteShiftWork(false);
    shiftWorkDTO.setId(null);
    List<ShiftWorkDTO> lstWo = Mockito.spy(ArrayList.class);
    lstWo.add(shiftWorkDTO);

    GnocFileDto gnocFileDto = Mockito.spy(GnocFileDto.class);
    gnocFileDto.setMappingId(1L);
    List<GnocFileDto> gnocFileDtosWeb = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 3; i++) {
      gnocFileDtosWeb.add(gnocFileDto);
    }
    UsersEntity userAssign = Mockito.spy(UsersEntity.class);
    userAssign.setUnitId(2L);
    when(userRepository.getUserByUserId(any())).thenReturn(userAssign);

    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setOpenToolTip(null);
    shiftHandoverDTO.setShiftStaftDTOList(lstStafts);
    shiftHandoverDTO.setShiftWorkDTOList(lstWo);
    shiftHandoverDTO.setShiftCrDTOList(lstCr);
    shiftHandoverDTO.setCreatedTimeOld(new Date());
    shiftHandoverDTO.setShiftCrDTOList(lstCr);
    shiftHandoverDTO.setGnocFileDtos(gnocFileDtosWeb);

    when(shiftHandoverRepository.updateCfgShiftHandover(any())).thenReturn(resultInSideDto);
    when(shiftStaftRepository.insertShiftUser(any())).thenReturn(resultInSideDto);
    when(shiftWorkRepository.insertShiftWork(any())).thenReturn(resultInSideDto);
    when(shiftCrRepository.insertShiftCr(any())).thenReturn(resultInSideDto);
    when(logChangeConfigBusiness
        .insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "UpdateCfgShiftHandover", "UpdateCfgShiftHandover ID: " + shiftHandoverDTO.getId(),
            shiftHandoverDTO, null))).thenReturn(resultInSideDto);

    UnitDTO unitToken = Mockito.spy(UnitDTO.class);
    when(unitRepository.findUnitById(any())).thenReturn(unitToken);

    ShiftHandoverFileEntity shiftHandoverFileEntity = Mockito.spy(ShiftHandoverFileEntity.class);
    List<ShiftHandoverFileEntity> lstFileOld = Mockito.spy(ArrayList.class);
    lstFileOld.add(shiftHandoverFileEntity);
    when(shiftHandoverRepository.findShiftHandOverFile(any())).thenReturn(lstFileOld);

    shiftHandoverBusiness.updateCfgShiftHandover(files, shiftHandoverDTO);
  }

  @Test
  public void findListShiftHandOverById() {
    Long id = 2L;
    PowerMockito.mockStatic(I18n.class);
    ShiftCrDTO shiftCrDTO = Mockito.spy(ShiftCrDTO.class);
    shiftCrDTO.setCrNumber("9_9");
    List<ShiftCrDTO> lstCr = Mockito.spy(ArrayList.class);
    lstCr.add(shiftCrDTO);

    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setShiftCrDTOList(lstCr);

    when(shiftHandoverRepository.findListShiftHandOverById(any())).thenReturn(shiftHandoverDTO);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setState("3");
    crDTO.setChangeResponsible("3");
    when(crServiceProxy.findCrByIdProxy(any())).thenReturn(crDTO);

    UsersEntity u = Mockito.spy(UsersEntity.class);
    when(userRepository.getUserByUserId(any())).thenReturn(u);

    shiftHandoverBusiness.findListShiftHandOverById(id);
  }

  @Test
  public void countTicketByShift() {
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setUnitId(2L);
    shiftHandoverDTO.setShiftValue("CA_1");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> lstKpi = Mockito.spy(ArrayList.class);
    lstKpi.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstKpi);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    CatItemDTO catItemDTOsetMap = Mockito.spy(CatItemDTO.class);
    catItemDTOsetMap.setItemValue("CA_3");
    List<CatItemDTO> catItemDTOsetMapList = Mockito.spy(ArrayList.class);
    catItemDTOsetMapList.add(catItemDTOsetMap);
    List<String> lstCategoryCode = new ArrayList<>();
    lstCategoryCode.add(Constants.GNOC_SHIFT);
    lstCategoryCode.add(Constants.GNOC_SHIFT_KPI);
    when(catItemRepository.getListCatItemDTO(lstCategoryCode, null))
        .thenReturn(catItemDTOsetMapList);

    ShiftHandoverDTO shiftHandoverDTOBefore = Mockito.spy(ShiftHandoverDTO.class);
    List<ShiftHandoverDTO> lstBefore = Mockito.spy(ArrayList.class);
    lstBefore.add(shiftHandoverDTOBefore);
    when(shiftHandoverRepository.getListShiftHandoverNew(any())).thenReturn(lstBefore);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setPriorityId(1952L);
    List<TroublesInSideDTO> lstTicket = Mockito.spy(ArrayList.class);
    lstTicket.add(troublesInSideDTO);
    when(ttServiceProxy.countTicketByShift(any())).thenReturn(lstTicket);

    shiftHandoverBusiness.countTicketByShift(shiftHandoverDTO);
  }

  @Test
  public void countTicketByShift1() {
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setUnitId(3L);
    shiftHandoverDTO.setShiftValue("CA_2");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> lstKpi = Mockito.spy(ArrayList.class);
    lstKpi.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstKpi);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    CatItemDTO catItemDTOsetMap = Mockito.spy(CatItemDTO.class);
    catItemDTOsetMap.setItemValue("CA_1");
    List<CatItemDTO> catItemDTOsetMapList = Mockito.spy(ArrayList.class);
    catItemDTOsetMapList.add(catItemDTOsetMap);
    List<String> lstCategoryCode = new ArrayList<>();
    lstCategoryCode.add(Constants.GNOC_SHIFT);
    lstCategoryCode.add(Constants.GNOC_SHIFT_KPI);
    when(catItemRepository.getListCatItemDTO(lstCategoryCode, null))
        .thenReturn(catItemDTOsetMapList);

    ShiftHandoverDTO shiftHandoverDTOBefore = Mockito.spy(ShiftHandoverDTO.class);
    List<ShiftHandoverDTO> lstBefore = Mockito.spy(ArrayList.class);
    lstBefore.add(shiftHandoverDTOBefore);
    when(shiftHandoverRepository.getListShiftHandoverNew(any())).thenReturn(lstBefore);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setPriorityId(1952L);
    List<TroublesInSideDTO> lstTicket = Mockito.spy(ArrayList.class);
    lstTicket.add(troublesInSideDTO);
    when(ttServiceProxy.countTicketByShift(any())).thenReturn(lstTicket);

    shiftHandoverBusiness.countTicketByShift(shiftHandoverDTO);
  }

  @Test
  public void countTicketByShift2() {
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setUnitId(3L);
    shiftHandoverDTO.setShiftValue("CA_3");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("3");
    List<CatItemDTO> lstKpi = Mockito.spy(ArrayList.class);
    lstKpi.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstKpi);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    CatItemDTO catItemDTOsetMap = Mockito.spy(CatItemDTO.class);
    catItemDTOsetMap.setItemValue("CA_2");
    List<CatItemDTO> catItemDTOsetMapList = Mockito.spy(ArrayList.class);
    catItemDTOsetMapList.add(catItemDTOsetMap);
    List<String> lstCategoryCode = new ArrayList<>();
    lstCategoryCode.add(Constants.GNOC_SHIFT);
    lstCategoryCode.add(Constants.GNOC_SHIFT_KPI);
    when(catItemRepository.getListCatItemDTO(lstCategoryCode, null))
        .thenReturn(catItemDTOsetMapList);

    ShiftHandoverDTO shiftHandoverDTOBefore = Mockito.spy(ShiftHandoverDTO.class);
    List<ShiftHandoverDTO> lstBefore = Mockito.spy(ArrayList.class);
    lstBefore.add(shiftHandoverDTOBefore);
    when(shiftHandoverRepository.getListShiftHandoverNew(any())).thenReturn(lstBefore);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2L);
    troublesInSideDTO.setPriorityId(1952L);
    TroublesInSideDTO troublesInSideDTO1 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO1.setTroubleId(2L);
    troublesInSideDTO1.setPriorityId(1000L);
    TroublesInSideDTO troublesInSideDTO2 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO2.setTroubleId(2L);
    troublesInSideDTO2.setPriorityId(61L);
    List<TroublesInSideDTO> lstTicket = Mockito.spy(ArrayList.class);
    lstTicket.add(troublesInSideDTO);
    lstTicket.add(troublesInSideDTO1);
    lstTicket.add(troublesInSideDTO2);
    when(ttServiceProxy.countTicketByShift(any())).thenReturn(lstTicket);

    shiftHandoverBusiness.countTicketByShift(shiftHandoverDTO);
  }

  @Test
  public void countTicketByShift3() {
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setUnitId(3L);
    shiftHandoverDTO.setShiftValue("CA_3");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("5");
    List<CatItemDTO> lstKpi = Mockito.spy(ArrayList.class);
    lstKpi.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstKpi);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    CatItemDTO catItemDTOsetMap = Mockito.spy(CatItemDTO.class);
    catItemDTOsetMap.setItemValue("CA_2");
    List<CatItemDTO> catItemDTOsetMapList = Mockito.spy(ArrayList.class);
    catItemDTOsetMapList.add(catItemDTOsetMap);
    List<String> lstCategoryCode = new ArrayList<>();
    lstCategoryCode.add(Constants.GNOC_SHIFT);
    lstCategoryCode.add(Constants.GNOC_SHIFT_KPI);
    when(catItemRepository.getListCatItemDTO(lstCategoryCode, null))
        .thenReturn(catItemDTOsetMapList);

    ShiftHandoverDTO shiftHandoverDTOBefore = Mockito.spy(ShiftHandoverDTO.class);
    List<ShiftHandoverDTO> lstBefore = Mockito.spy(ArrayList.class);
    lstBefore.add(shiftHandoverDTOBefore);
    when(shiftHandoverRepository.getListShiftHandoverNew(any())).thenReturn(lstBefore);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleCode("2");
    troublesInSideDTO.setPriorityId(1952L);
    TroublesInSideDTO troublesInSideDTO1 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO1.setTroubleCode("2");
    troublesInSideDTO1.setPriorityId(1000L);
    TroublesInSideDTO troublesInSideDTO2 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO2.setTroubleCode("2");
    troublesInSideDTO2.setPriorityId(61L);
    List<TroublesInSideDTO> lstTicket = Mockito.spy(ArrayList.class);
    lstTicket.add(troublesInSideDTO);
    lstTicket.add(troublesInSideDTO1);
    lstTicket.add(troublesInSideDTO2);
    when(ttServiceProxy.countTicketByShift(any())).thenReturn(lstTicket);

    shiftHandoverBusiness.countTicketByShift(shiftHandoverDTO);
  }

  @Test
  public void countTicketByShift4() {
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setUnitId(3L);
    shiftHandoverDTO.setShiftValue("CA_3");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("6");
    List<CatItemDTO> lstKpi = Mockito.spy(ArrayList.class);
    lstKpi.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstKpi);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    CatItemDTO catItemDTOsetMap = Mockito.spy(CatItemDTO.class);
    catItemDTOsetMap.setItemValue("CA_2");
    List<CatItemDTO> catItemDTOsetMapList = Mockito.spy(ArrayList.class);
    catItemDTOsetMapList.add(catItemDTOsetMap);
    List<String> lstCategoryCode = new ArrayList<>();
    lstCategoryCode.add(Constants.GNOC_SHIFT);
    lstCategoryCode.add(Constants.GNOC_SHIFT_KPI);
    when(catItemRepository.getListCatItemDTO(lstCategoryCode, null))
        .thenReturn(catItemDTOsetMapList);

    ShiftHandoverDTO shiftHandoverDTOBefore = Mockito.spy(ShiftHandoverDTO.class);
    List<ShiftHandoverDTO> lstBefore = Mockito.spy(ArrayList.class);
    lstBefore.add(shiftHandoverDTOBefore);
    when(shiftHandoverRepository.getListShiftHandoverNew(any())).thenReturn(lstBefore);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleName("2");
    troublesInSideDTO.setPriorityId(1952L);
    TroublesInSideDTO troublesInSideDTO1 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO1.setTroubleName("2");
    troublesInSideDTO1.setPriorityId(1000L);
    TroublesInSideDTO troublesInSideDTO2 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO2.setTroubleName("2");
    troublesInSideDTO2.setPriorityId(61L);
    List<TroublesInSideDTO> lstTicket = Mockito.spy(ArrayList.class);
    lstTicket.add(troublesInSideDTO);
    lstTicket.add(troublesInSideDTO1);
    lstTicket.add(troublesInSideDTO2);
    when(ttServiceProxy.countTicketByShift(any())).thenReturn(lstTicket);

    shiftHandoverBusiness.countTicketByShift(shiftHandoverDTO);
  }

  @Test
  public void countTicketByShift5() {
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setUnitId(3L);
    shiftHandoverDTO.setShiftValue("CA_3");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("7");
    List<CatItemDTO> lstKpi = Mockito.spy(ArrayList.class);
    lstKpi.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstKpi);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    CatItemDTO catItemDTOsetMap = Mockito.spy(CatItemDTO.class);
    catItemDTOsetMap.setItemValue("CA_2");
    List<CatItemDTO> catItemDTOsetMapList = Mockito.spy(ArrayList.class);
    catItemDTOsetMapList.add(catItemDTOsetMap);
    List<String> lstCategoryCode = new ArrayList<>();
    lstCategoryCode.add(Constants.GNOC_SHIFT);
    lstCategoryCode.add(Constants.GNOC_SHIFT_KPI);
    when(catItemRepository.getListCatItemDTO(lstCategoryCode, null))
        .thenReturn(catItemDTOsetMapList);

    ShiftHandoverDTO shiftHandoverDTOBefore = Mockito.spy(ShiftHandoverDTO.class);
    List<ShiftHandoverDTO> lstBefore = Mockito.spy(ArrayList.class);
    lstBefore.add(shiftHandoverDTOBefore);
    when(shiftHandoverRepository.getListShiftHandoverNew(any())).thenReturn(lstBefore);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setDescription("2");
    troublesInSideDTO.setPriorityId(1952L);
    TroublesInSideDTO troublesInSideDTO1 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO1.setDescription("2");
    troublesInSideDTO1.setPriorityId(1000L);
    TroublesInSideDTO troublesInSideDTO2 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO2.setDescription("2");
    troublesInSideDTO2.setPriorityId(61L);
    List<TroublesInSideDTO> lstTicket = Mockito.spy(ArrayList.class);
    lstTicket.add(troublesInSideDTO);
    lstTicket.add(troublesInSideDTO1);
    lstTicket.add(troublesInSideDTO2);
    when(ttServiceProxy.countTicketByShift(any())).thenReturn(lstTicket);

    shiftHandoverBusiness.countTicketByShift(shiftHandoverDTO);
  }

  @Test
  public void countTicketByShift6() {
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setUnitId(3L);
    shiftHandoverDTO.setShiftValue("CA_3");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("9");
    List<CatItemDTO> lstKpi = Mockito.spy(ArrayList.class);
    lstKpi.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstKpi);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    CatItemDTO catItemDTOsetMap = Mockito.spy(CatItemDTO.class);
    catItemDTOsetMap.setItemValue("CA_2");
    List<CatItemDTO> catItemDTOsetMapList = Mockito.spy(ArrayList.class);
    catItemDTOsetMapList.add(catItemDTOsetMap);
    List<String> lstCategoryCode = new ArrayList<>();
    lstCategoryCode.add(Constants.GNOC_SHIFT);
    lstCategoryCode.add(Constants.GNOC_SHIFT_KPI);
    when(catItemRepository.getListCatItemDTO(lstCategoryCode, null))
        .thenReturn(catItemDTOsetMapList);

    ShiftHandoverDTO shiftHandoverDTOBefore = Mockito.spy(ShiftHandoverDTO.class);
    List<ShiftHandoverDTO> lstBefore = Mockito.spy(ArrayList.class);
    lstBefore.add(shiftHandoverDTOBefore);
    when(shiftHandoverRepository.getListShiftHandoverNew(any())).thenReturn(lstBefore);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setSubCategoryId(2L);
    troublesInSideDTO.setPriorityId(1952L);
    TroublesInSideDTO troublesInSideDTO1 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO1.setSubCategoryId(2L);
    troublesInSideDTO1.setPriorityId(1000L);
    TroublesInSideDTO troublesInSideDTO2 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO2.setSubCategoryId(2L);
    troublesInSideDTO2.setPriorityId(61L);
    List<TroublesInSideDTO> lstTicket = Mockito.spy(ArrayList.class);
    lstTicket.add(troublesInSideDTO);
    lstTicket.add(troublesInSideDTO1);
    lstTicket.add(troublesInSideDTO2);
    when(ttServiceProxy.countTicketByShift(any())).thenReturn(lstTicket);

    shiftHandoverBusiness.countTicketByShift(shiftHandoverDTO);
  }

  @Test
  public void countTicketByShift7() {
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setUnitId(3L);
    shiftHandoverDTO.setShiftValue("CA_3");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("2");
    List<CatItemDTO> lstKpi = Mockito.spy(ArrayList.class);
    lstKpi.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstKpi);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    CatItemDTO catItemDTOsetMap = Mockito.spy(CatItemDTO.class);
    catItemDTOsetMap.setItemValue("CA_2");
    List<CatItemDTO> catItemDTOsetMapList = Mockito.spy(ArrayList.class);
    catItemDTOsetMapList.add(catItemDTOsetMap);
    List<String> lstCategoryCode = new ArrayList<>();
    lstCategoryCode.add(Constants.GNOC_SHIFT);
    lstCategoryCode.add(Constants.GNOC_SHIFT_KPI);
    when(catItemRepository.getListCatItemDTO(lstCategoryCode, null))
        .thenReturn(catItemDTOsetMapList);

    ShiftHandoverDTO shiftHandoverDTOBefore = Mockito.spy(ShiftHandoverDTO.class);
    List<ShiftHandoverDTO> lstBefore = Mockito.spy(ArrayList.class);
    lstBefore.add(shiftHandoverDTOBefore);
    when(shiftHandoverRepository.getListShiftHandoverNew(any())).thenReturn(lstBefore);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setSubCategoryId(2L);
    troublesInSideDTO.setPriorityId(1952L);
    TroublesInSideDTO troublesInSideDTO1 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO1.setSubCategoryId(2L);
    troublesInSideDTO1.setPriorityId(1000L);
    TroublesInSideDTO troublesInSideDTO2 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO2.setSubCategoryId(2L);
    troublesInSideDTO2.setPriorityId(61L);
    List<TroublesInSideDTO> lstTicket = Mockito.spy(ArrayList.class);
    lstTicket.add(troublesInSideDTO);
    lstTicket.add(troublesInSideDTO1);
    lstTicket.add(troublesInSideDTO2);
    when(ttServiceProxy.countTicketByShift(any())).thenReturn(lstTicket);

    ShiftItDTO shiftItDTO = Mockito.spy(ShiftItDTO.class);
    List<ShiftItDTO> lstDTO = Mockito.spy(ArrayList.class);
    lstDTO.add(shiftItDTO);
    when(shiftItRepository.getListShiftItByShiftID(any())).thenReturn(lstDTO);

    shiftHandoverBusiness.countTicketByShift(shiftHandoverDTO);
  }

  @Test
  public void countTicketByShift8() {
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setUnitId(3L);
    shiftHandoverDTO.setShiftValue("CA_3");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("1");
    List<CatItemDTO> lstKpi = Mockito.spy(ArrayList.class);
    lstKpi.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstKpi);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    CatItemDTO catItemDTOsetMap = Mockito.spy(CatItemDTO.class);
    catItemDTOsetMap.setItemValue("CA_2");
    List<CatItemDTO> catItemDTOsetMapList = Mockito.spy(ArrayList.class);
    catItemDTOsetMapList.add(catItemDTOsetMap);
    List<String> lstCategoryCode = new ArrayList<>();
    lstCategoryCode.add(Constants.GNOC_SHIFT);
    lstCategoryCode.add(Constants.GNOC_SHIFT_KPI);
    when(catItemRepository.getListCatItemDTO(lstCategoryCode, null))
        .thenReturn(catItemDTOsetMapList);

    ShiftHandoverDTO shiftHandoverDTOBefore = Mockito.spy(ShiftHandoverDTO.class);
    List<ShiftHandoverDTO> lstBefore = Mockito.spy(ArrayList.class);
    lstBefore.add(shiftHandoverDTOBefore);
    when(shiftHandoverRepository.getListShiftHandoverNew(any())).thenReturn(lstBefore);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setSubCategoryId(2L);
    troublesInSideDTO.setPriorityId(1952L);
    TroublesInSideDTO troublesInSideDTO1 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO1.setSubCategoryId(2L);
    troublesInSideDTO1.setPriorityId(1000L);
    TroublesInSideDTO troublesInSideDTO2 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO2.setSubCategoryId(2L);
    troublesInSideDTO2.setPriorityId(61L);
    List<TroublesInSideDTO> lstTicket = Mockito.spy(ArrayList.class);
    lstTicket.add(troublesInSideDTO);
    lstTicket.add(troublesInSideDTO1);
    lstTicket.add(troublesInSideDTO2);
    when(ttServiceProxy.countTicketByShift(any())).thenReturn(lstTicket);

    ShiftItDTO shiftItDTO = Mockito.spy(ShiftItDTO.class);
    List<ShiftItDTO> lstDTO = Mockito.spy(ArrayList.class);
    lstDTO.add(shiftItDTO);
    when(shiftItRepository.getListShiftItByShiftID(any())).thenReturn(lstDTO);

    shiftHandoverBusiness.countTicketByShift(shiftHandoverDTO);
  }

  @Test
  public void countTicketByShift9() {
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setUnitId(3L);
    shiftHandoverDTO.setShiftValue("CA_3");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("3");
    List<CatItemDTO> lstKpi = Mockito.spy(ArrayList.class);
    lstKpi.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstKpi);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    CatItemDTO catItemDTOsetMap = Mockito.spy(CatItemDTO.class);
    catItemDTOsetMap.setItemValue("CA_2");
    List<CatItemDTO> catItemDTOsetMapList = Mockito.spy(ArrayList.class);
    catItemDTOsetMapList.add(catItemDTOsetMap);
    List<String> lstCategoryCode = new ArrayList<>();
    lstCategoryCode.add(Constants.GNOC_SHIFT);
    lstCategoryCode.add(Constants.GNOC_SHIFT_KPI);
    when(catItemRepository.getListCatItemDTO(lstCategoryCode, null))
        .thenReturn(catItemDTOsetMapList);

    ShiftHandoverDTO shiftHandoverDTOBefore = Mockito.spy(ShiftHandoverDTO.class);
    List<ShiftHandoverDTO> lstBefore = Mockito.spy(ArrayList.class);
    lstBefore.add(shiftHandoverDTOBefore);
    when(shiftHandoverRepository.getListShiftHandoverNew(any())).thenReturn(lstBefore);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleId(2L);
    troublesInSideDTO.setPriorityId(1952L);
    TroublesInSideDTO troublesInSideDTO1 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO1.setTroubleId(2L);
    troublesInSideDTO1.setPriorityId(1000L);
    TroublesInSideDTO troublesInSideDTO2 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO2.setTroubleId(2L);
    troublesInSideDTO2.setPriorityId(61L);
    List<TroublesInSideDTO> lstTicket = Mockito.spy(ArrayList.class);
    lstTicket.add(troublesInSideDTO);
    lstTicket.add(troublesInSideDTO1);
    lstTicket.add(troublesInSideDTO2);
    when(ttServiceProxy.countTicketByShift(any())).thenReturn(lstTicket);

    ShiftItDTO shiftItDTO = Mockito.spy(ShiftItDTO.class);
    List<ShiftItDTO> lstDTO = Mockito.spy(ArrayList.class);
    lstDTO.add(shiftItDTO);
    when(shiftItRepository.getListShiftItByShiftID(any())).thenReturn(lstDTO);

    shiftHandoverBusiness.countTicketByShift(shiftHandoverDTO);
  }

  @Test
  public void countTicketByShift10() {
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setUnitId(3L);
    shiftHandoverDTO.setShiftValue("CA_3");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("4");
    List<CatItemDTO> lstKpi = Mockito.spy(ArrayList.class);
    lstKpi.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstKpi);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    CatItemDTO catItemDTOsetMap = Mockito.spy(CatItemDTO.class);
    catItemDTOsetMap.setItemValue("CA_2");
    List<CatItemDTO> catItemDTOsetMapList = Mockito.spy(ArrayList.class);
    catItemDTOsetMapList.add(catItemDTOsetMap);
    List<String> lstCategoryCode = new ArrayList<>();
    lstCategoryCode.add(Constants.GNOC_SHIFT);
    lstCategoryCode.add(Constants.GNOC_SHIFT_KPI);
    when(catItemRepository.getListCatItemDTO(lstCategoryCode, null))
        .thenReturn(catItemDTOsetMapList);

    ShiftHandoverDTO shiftHandoverDTOBefore = Mockito.spy(ShiftHandoverDTO.class);
    List<ShiftHandoverDTO> lstBefore = Mockito.spy(ArrayList.class);
    lstBefore.add(shiftHandoverDTOBefore);
    when(shiftHandoverRepository.getListShiftHandoverNew(any())).thenReturn(lstBefore);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setSubCategoryId(2L);
    troublesInSideDTO.setPriorityId(1952L);
    TroublesInSideDTO troublesInSideDTO1 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO1.setSubCategoryId(2L);
    troublesInSideDTO1.setPriorityId(1000L);
    TroublesInSideDTO troublesInSideDTO2 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO2.setSubCategoryId(2L);
    troublesInSideDTO2.setPriorityId(61L);
    List<TroublesInSideDTO> lstTicket = Mockito.spy(ArrayList.class);
    lstTicket.add(troublesInSideDTO);
    lstTicket.add(troublesInSideDTO1);
    lstTicket.add(troublesInSideDTO2);
    when(ttServiceProxy.countTicketByShift(any())).thenReturn(lstTicket);

    ShiftItDTO shiftItDTO = Mockito.spy(ShiftItDTO.class);
    List<ShiftItDTO> lstDTO = Mockito.spy(ArrayList.class);
    lstDTO.add(shiftItDTO);
    when(shiftItRepository.getListShiftItByShiftID(any())).thenReturn(lstDTO);

    shiftHandoverBusiness.countTicketByShift(shiftHandoverDTO);
  }

  @Test
  public void countTicketByShift11() {
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setUnitId(3L);
    shiftHandoverDTO.setShiftValue("CA_3");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("5");
    List<CatItemDTO> lstKpi = Mockito.spy(ArrayList.class);
    lstKpi.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstKpi);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    CatItemDTO catItemDTOsetMap = Mockito.spy(CatItemDTO.class);
    catItemDTOsetMap.setItemValue("CA_2");
    List<CatItemDTO> catItemDTOsetMapList = Mockito.spy(ArrayList.class);
    catItemDTOsetMapList.add(catItemDTOsetMap);
    List<String> lstCategoryCode = new ArrayList<>();
    lstCategoryCode.add(Constants.GNOC_SHIFT);
    lstCategoryCode.add(Constants.GNOC_SHIFT_KPI);
    when(catItemRepository.getListCatItemDTO(lstCategoryCode, null))
        .thenReturn(catItemDTOsetMapList);

    ShiftHandoverDTO shiftHandoverDTOBefore = Mockito.spy(ShiftHandoverDTO.class);
    List<ShiftHandoverDTO> lstBefore = Mockito.spy(ArrayList.class);
    lstBefore.add(shiftHandoverDTOBefore);
    when(shiftHandoverRepository.getListShiftHandoverNew(any())).thenReturn(lstBefore);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setTroubleCode("2");
    troublesInSideDTO.setPriorityId(1952L);
    TroublesInSideDTO troublesInSideDTO1 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO1.setTroubleCode("2");
    troublesInSideDTO1.setPriorityId(1000L);
    TroublesInSideDTO troublesInSideDTO2 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO2.setTroubleCode("2");
    troublesInSideDTO2.setPriorityId(61L);
    List<TroublesInSideDTO> lstTicket = Mockito.spy(ArrayList.class);
    lstTicket.add(troublesInSideDTO);
    lstTicket.add(troublesInSideDTO1);
    lstTicket.add(troublesInSideDTO2);
    when(ttServiceProxy.countTicketByShift(any())).thenReturn(lstTicket);

    ShiftItDTO shiftItDTO = Mockito.spy(ShiftItDTO.class);
    List<ShiftItDTO> lstDTO = Mockito.spy(ArrayList.class);
    lstDTO.add(shiftItDTO);
    when(shiftItRepository.getListShiftItByShiftID(any())).thenReturn(lstDTO);

    shiftHandoverBusiness.countTicketByShift(shiftHandoverDTO);
  }

  @Test
  public void countTicketByShift12() {
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setUnitId(3L);
    shiftHandoverDTO.setShiftValue("CA_3");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("8");
    List<CatItemDTO> lstKpi = Mockito.spy(ArrayList.class);
    lstKpi.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstKpi);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    CatItemDTO catItemDTOsetMap = Mockito.spy(CatItemDTO.class);
    catItemDTOsetMap.setItemValue("CA_2");
    List<CatItemDTO> catItemDTOsetMapList = Mockito.spy(ArrayList.class);
    catItemDTOsetMapList.add(catItemDTOsetMap);
    List<String> lstCategoryCode = new ArrayList<>();
    lstCategoryCode.add(Constants.GNOC_SHIFT);
    lstCategoryCode.add(Constants.GNOC_SHIFT_KPI);
    when(catItemRepository.getListCatItemDTO(lstCategoryCode, null))
        .thenReturn(catItemDTOsetMapList);

    ShiftHandoverDTO shiftHandoverDTOBefore = Mockito.spy(ShiftHandoverDTO.class);
    List<ShiftHandoverDTO> lstBefore = Mockito.spy(ArrayList.class);
    lstBefore.add(shiftHandoverDTOBefore);
    when(shiftHandoverRepository.getListShiftHandoverNew(any())).thenReturn(lstBefore);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setSubCategoryId(2L);
    troublesInSideDTO.setPriorityId(1952L);
    TroublesInSideDTO troublesInSideDTO1 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO1.setSubCategoryId(2L);
    troublesInSideDTO1.setPriorityId(1000L);
    TroublesInSideDTO troublesInSideDTO2 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO2.setSubCategoryId(2L);
    troublesInSideDTO2.setPriorityId(61L);
    List<TroublesInSideDTO> lstTicket = Mockito.spy(ArrayList.class);
    lstTicket.add(troublesInSideDTO);
    lstTicket.add(troublesInSideDTO1);
    lstTicket.add(troublesInSideDTO2);
    when(ttServiceProxy.countTicketByShift(any())).thenReturn(lstTicket);

    ShiftItDTO shiftItDTO = Mockito.spy(ShiftItDTO.class);
    List<ShiftItDTO> lstDTO = Mockito.spy(ArrayList.class);
    lstDTO.add(shiftItDTO);
    when(shiftItRepository.getListShiftItByShiftID(any())).thenReturn(lstDTO);

    shiftHandoverBusiness.countTicketByShift(shiftHandoverDTO);
  }

  @Test
  public void countTicketByShift13() {
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setUnitId(3L);
    shiftHandoverDTO.setShiftValue("CA_3");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("10");
    List<CatItemDTO> lstKpi = Mockito.spy(ArrayList.class);
    lstKpi.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstKpi);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    CatItemDTO catItemDTOsetMap = Mockito.spy(CatItemDTO.class);
    catItemDTOsetMap.setItemValue("CA_2");
    List<CatItemDTO> catItemDTOsetMapList = Mockito.spy(ArrayList.class);
    catItemDTOsetMapList.add(catItemDTOsetMap);
    List<String> lstCategoryCode = new ArrayList<>();
    lstCategoryCode.add(Constants.GNOC_SHIFT);
    lstCategoryCode.add(Constants.GNOC_SHIFT_KPI);
    when(catItemRepository.getListCatItemDTO(lstCategoryCode, null))
        .thenReturn(catItemDTOsetMapList);

    ShiftHandoverDTO shiftHandoverDTOBefore = Mockito.spy(ShiftHandoverDTO.class);
    List<ShiftHandoverDTO> lstBefore = Mockito.spy(ArrayList.class);
    lstBefore.add(shiftHandoverDTOBefore);
    when(shiftHandoverRepository.getListShiftHandoverNew(any())).thenReturn(lstBefore);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setSubCategoryId(2L);
    troublesInSideDTO.setPriorityId(1952L);
    TroublesInSideDTO troublesInSideDTO1 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO1.setSubCategoryId(2L);
    troublesInSideDTO1.setPriorityId(1000L);
    TroublesInSideDTO troublesInSideDTO2 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO2.setSubCategoryId(2L);
    troublesInSideDTO2.setPriorityId(61L);
    List<TroublesInSideDTO> lstTicket = Mockito.spy(ArrayList.class);
    lstTicket.add(troublesInSideDTO);
    lstTicket.add(troublesInSideDTO1);
    lstTicket.add(troublesInSideDTO2);
    when(ttServiceProxy.countTicketByShift(any())).thenReturn(lstTicket);

    ShiftItDTO shiftItDTO = Mockito.spy(ShiftItDTO.class);
    List<ShiftItDTO> lstDTO = Mockito.spy(ArrayList.class);
    lstDTO.add(shiftItDTO);
    when(shiftItRepository.getListShiftItByShiftID(any())).thenReturn(lstDTO);

    shiftHandoverBusiness.countTicketByShift(shiftHandoverDTO);
  }

  @Test
  public void countTicketByShift14() {
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setUnitId(3L);
    shiftHandoverDTO.setShiftValue("CA_3");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("11");
    List<CatItemDTO> lstKpi = Mockito.spy(ArrayList.class);
    lstKpi.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstKpi);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    CatItemDTO catItemDTOsetMap = Mockito.spy(CatItemDTO.class);
    catItemDTOsetMap.setItemValue("CA_2");
    List<CatItemDTO> catItemDTOsetMapList = Mockito.spy(ArrayList.class);
    catItemDTOsetMapList.add(catItemDTOsetMap);
    List<String> lstCategoryCode = new ArrayList<>();
    lstCategoryCode.add(Constants.GNOC_SHIFT);
    lstCategoryCode.add(Constants.GNOC_SHIFT_KPI);
    when(catItemRepository.getListCatItemDTO(lstCategoryCode, null))
        .thenReturn(catItemDTOsetMapList);

    ShiftHandoverDTO shiftHandoverDTOBefore = Mockito.spy(ShiftHandoverDTO.class);
    List<ShiftHandoverDTO> lstBefore = Mockito.spy(ArrayList.class);
    lstBefore.add(shiftHandoverDTOBefore);
    when(shiftHandoverRepository.getListShiftHandoverNew(any())).thenReturn(lstBefore);

    TroublesInSideDTO troublesInSideDTO = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO.setSubCategoryId(2L);
    troublesInSideDTO.setPriorityId(1952L);
    TroublesInSideDTO troublesInSideDTO1 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO1.setSubCategoryId(2L);
    troublesInSideDTO1.setPriorityId(1000L);
    TroublesInSideDTO troublesInSideDTO2 = Mockito.spy(TroublesInSideDTO.class);
    troublesInSideDTO2.setSubCategoryId(2L);
    troublesInSideDTO2.setPriorityId(61L);
    List<TroublesInSideDTO> lstTicket = Mockito.spy(ArrayList.class);
    lstTicket.add(troublesInSideDTO);
    lstTicket.add(troublesInSideDTO1);
    lstTicket.add(troublesInSideDTO2);
    when(ttServiceProxy.countTicketByShift(any())).thenReturn(lstTicket);

    ShiftItDTO shiftItDTO = Mockito.spy(ShiftItDTO.class);
    List<ShiftItDTO> lstDTO = Mockito.spy(ArrayList.class);
    lstDTO.add(shiftItDTO);
    when(shiftItRepository.getListShiftItByShiftID(any())).thenReturn(lstDTO);

    shiftHandoverBusiness.countTicketByShift(shiftHandoverDTO);
  }

  @Test
  public void countTicketByShift17() {
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setShiftValue("-1");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    List<CatItemDTO> lstKpi = Mockito.spy(ArrayList.class);
    lstKpi.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstKpi);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    shiftHandoverBusiness.countTicketByShift(shiftHandoverDTO);
  }


  @Test
  public void exportShiftRow() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setShiftValue("2");
    shiftHandoverDTO.setUnitName("2");
    shiftHandoverDTO.setShiftName("2");
    shiftHandoverDTO.setShiftId(3L);

    List<ShiftItDTO> lstIt = null;
    Datatable datatableShiftIT = Mockito.spy(Datatable.class);
    datatableShiftIT.setData(lstIt);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatableShiftIT);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("2");
    List<CatItemDTO> lstShift = Mockito.spy(ArrayList.class);
    lstShift.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstShift);

    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    shiftHandoverBusiness.exportShiftRow(shiftHandoverDTO);
  }

  @Test
  public void exportShiftRow1() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setShiftValue("CA_1");
    shiftHandoverDTO.setUnitName("2");
    shiftHandoverDTO.setShiftName("2");
    shiftHandoverDTO.setShiftId(1341L);

    List<ShiftItDTO> lstIt = null;
    Datatable datatableShiftIT = Mockito.spy(Datatable.class);
    datatableShiftIT.setData(lstIt);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatableShiftIT);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("CA_2");
    List<CatItemDTO> lstShift = Mockito.spy(ArrayList.class);
    lstShift.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstShift);

    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    shiftHandoverBusiness.exportShiftRow(shiftHandoverDTO);
  }

  @Test
  public void exportShiftRow2() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setShiftValue("CA_2");
    shiftHandoverDTO.setUnitName("3");
    shiftHandoverDTO.setShiftName("2");
    shiftHandoverDTO.setShiftId(1342L);

    List<ShiftItDTO> lstIt = null;
    Datatable datatableShiftIT = Mockito.spy(Datatable.class);
    datatableShiftIT.setData(lstIt);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatableShiftIT);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("CA_3");
    List<CatItemDTO> lstShift = Mockito.spy(ArrayList.class);
    lstShift.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstShift);

    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    shiftHandoverBusiness.exportShiftRow(shiftHandoverDTO);
  }

  @Test
  public void exportShiftRow3() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setShiftValue("CA_3");
    shiftHandoverDTO.setUnitName("2");
    shiftHandoverDTO.setShiftName("2");
    shiftHandoverDTO.setShiftId(1343L);

    List<ShiftItDTO> lstIt = null;
    Datatable datatableShiftIT = Mockito.spy(Datatable.class);
    datatableShiftIT.setData(lstIt);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatableShiftIT);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("CA_1");
    List<CatItemDTO> lstShift = Mockito.spy(ArrayList.class);
    lstShift.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstShift);

    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    shiftHandoverBusiness.exportShiftRow(shiftHandoverDTO);
  }

  @Test
  public void exportDataFromRow() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setShiftValue("CA_3");
    shiftHandoverDTO.setUnitName("2");
    shiftHandoverDTO.setShiftName("2");
    shiftHandoverDTO.setShiftId(1343L);
    shiftHandoverDTO.setId(1L);

    ShiftStaftDTO shiftStaftDTO = Mockito.spy(ShiftStaftDTO.class);
    shiftStaftDTO.setId(1L);
    List<ShiftStaftDTO> lstIt = Mockito.spy(ArrayList.class);
    lstIt.add(shiftStaftDTO);
    Datatable datatableShiftIT = Mockito.spy(Datatable.class);
    datatableShiftIT.setData(lstIt);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatableShiftIT);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("CA_1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstShift = Mockito.spy(ArrayList.class);
    lstShift.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstShift);

    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    when(shiftStaftRepository.getListShiftStaftById(anyLong())).thenReturn(lstIt);

    ShiftWorkDTO shiftWorkDTO = Mockito.spy(ShiftWorkDTO.class);
    shiftWorkDTO.setDescription("2");
    shiftWorkDTO.setStartTime(new Date());
    shiftWorkDTO.setDeadLine(new Date());
    shiftWorkDTO.setOwner("2");
    shiftWorkDTO.setHandle("2");
    shiftWorkDTO.setImportantLevel("2");
    shiftWorkDTO.setResult("2");
    shiftWorkDTO.setNextWork("2");
    shiftWorkDTO.setContact("2");
    shiftWorkDTO.setWorkStatus("2");
    shiftWorkDTO.setOpinion("2");
    List<ShiftWorkDTO> lstWork = Mockito.spy(ArrayList.class);
    lstWork.add(shiftWorkDTO);
    when(shiftWorkRepository.getListShiftWorkByShiftID(any())).thenReturn(lstWork);

    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    ShiftItDTO shiftItDTO = Mockito.spy(ShiftItDTO.class);
    List<ShiftItDTO> lstDTO = Mockito.spy(ArrayList.class);
    lstDTO.add(shiftItDTO);
    when(shiftItRepository.getListShiftItByShiftID(any())).thenReturn(lstDTO);

    ShiftCrDTO shiftCrDTO = Mockito.spy(ShiftCrDTO.class);
    shiftCrDTO.setCrNumber("2_2");
    List<ShiftCrDTO> lstCR = Mockito.spy(ArrayList.class);
    lstCR.add(shiftCrDTO);
    when(shiftCrRepository.getListShiftCrByShiftID(any())).thenReturn(lstCR);

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setState("2");
    crDTO.setChangeResponsible("2");
    when(crServiceProxy.findCrByIdProxy((any()))).thenReturn(crDTO);

    UsersEntity u = Mockito.spy(UsersEntity.class);
    when(userRepository.getUserByUserId(any())).thenReturn(u);

    shiftHandoverBusiness.exportShiftRow(shiftHandoverDTO);
  }

  @Test
  public void exportDataFromRow1() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    ShiftHandoverDTO shiftHandoverDTO = Mockito.spy(ShiftHandoverDTO.class);
    shiftHandoverDTO.setCreatedTime(new Date());
    shiftHandoverDTO.setShiftValue("CA_3");
    shiftHandoverDTO.setUnitName("2");
    shiftHandoverDTO.setShiftName("2");
    shiftHandoverDTO.setShiftId(1343L);
    shiftHandoverDTO.setId(1L);

    ShiftStaftDTO shiftStaftDTO = Mockito.spy(ShiftStaftDTO.class);
    shiftStaftDTO.setId(1L);
    List<ShiftStaftDTO> lstIt = null;
    Datatable datatableShiftIT = Mockito.spy(Datatable.class);
    datatableShiftIT.setData(lstIt);
    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatableShiftIT);

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemValue("CA_1");
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstShift = Mockito.spy(ArrayList.class);
    lstShift.add(catItemDTO);
    Datatable datatable = Mockito.spy(Datatable.class);
    datatable.setData(lstShift);

    when(catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(datatable);

    List<ShiftStaftDTO> lstItelse = null;
    when(shiftStaftRepository.getListShiftStaftById(anyLong())).thenReturn(lstItelse);

    List<ShiftWorkDTO> lstWork = null;
    when(shiftWorkRepository.getListShiftWorkByShiftID(any())).thenReturn(lstWork);

    List<ShiftCrDTO> lstCR = null;
    when(shiftCrRepository.getListShiftCrByShiftID(any())).thenReturn(lstCR);

    shiftHandoverBusiness.exportShiftRow(shiftHandoverDTO);
  }

  @Test
  public void findCrByCrNumber() {
    PowerMockito.mockStatic(I18n.class);
    ShiftCrDTO shiftCrDTO = null;
    shiftHandoverBusiness.findCrByCrNumber(shiftCrDTO);
  }

  @Test
  public void findCrByCrNumber1() {
    PowerMockito.mockStatic(I18n.class);
    ShiftCrDTO shiftCrDTO = Mockito.spy(ShiftCrDTO.class);
    shiftCrDTO.setCrNumber("2_2");

    CrInsiteDTO crDTO = null;
    when(crServiceProxy.findCrByIdProxy(any())).thenReturn(crDTO);

    shiftHandoverBusiness.findCrByCrNumber(shiftCrDTO);
  }

  @Test
  public void findCrByCrNumber2() {
    PowerMockito.mockStatic(I18n.class);
    ShiftCrDTO shiftCrDTO = Mockito.spy(ShiftCrDTO.class);
    shiftCrDTO.setCrNumber("2_2_2");

    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
    crDTO.setCreatedDate(DateTimeUtils.convertStringToDate("01/01/2020 12:12:12"));
    when(crServiceProxy.findCrByIdProxy(any())).thenReturn(crDTO);

    shiftHandoverBusiness.findCrByCrNumber(shiftCrDTO);
  }

//  @Test
//  public void findCrByCrNumber3() {
//    ShiftCrDTO shiftCrDTO = Mockito.spy(ShiftCrDTO.class);
//    shiftCrDTO.setCrNumber("2_2_2");
//
//    CrInsiteDTO crDTO = Mockito.spy(CrInsiteDTO.class);
//    crDTO.setCreatedDate(DateTimeUtils.convertStringToDate("01/04/2020 12:12:12"));
//    when(crServiceProxy.findCrByIdProxy(any())).thenReturn(crDTO);
//
//    shiftHandoverBusiness.findCrByCrNumber(shiftCrDTO);
//  }

  @Test
  public void doExportExcel() {
  }

  @Test
  public void replaceNumberValue() {
  }

  @Test
  public void replaceStringValue() {
  }

  @Test
  public void getSequenseShiftHandover() {
  }

  @Test
  public void getTemplate() {
  }

  @Test
  public void getTemplateCR() {
  }

  @Test
  public void importShiftWorkData() {
  }

  @Test
  public void importShiftCRData() {
  }

  @Test
  public void setMapShift() {
  }
}
