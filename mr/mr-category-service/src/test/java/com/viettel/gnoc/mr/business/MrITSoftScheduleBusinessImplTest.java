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
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftProcedureDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrITSoftScheduleRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
import com.viettel.gnoc.mr.repository.MrServiceRepository;
import com.viettel.gnoc.mr.repository.MrSynItSoftDevicesRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;


@RunWith(PowerMockRunner.class)
@PrepareForTest({MrITSoftScheduleBusinessImpl.class, FileUtils.class, CommonImport.class,
    I18n.class,
    TicketProvider.class, DataUtil.class, CommonExport.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})

public class MrITSoftScheduleBusinessImplTest {

  @InjectMocks
  MrITSoftScheduleBusinessImpl mrITSoftScheduleBusiness;

  @Mock
  MrITSoftScheduleRepository mrITSoftScheduleRepository;

  @Mock
  MrSynItSoftDevicesRepository mrSynItSoftDevicesRepository;

  @Mock
  MrITSoftProcedureBusiness mrITSoftProcedureBusiness;

  @Mock
  MrITHisBusiness mrITHisBusiness;

  @Mock
  MrScheduleTelRepository mrScheduleTelRepository;

  @Mock
  CrServiceProxy crServiceProxy;

  @Mock
  MaintenanceMngtBusiness maintenanceMngtBusiness;

  @Mock
  CatLocationBusiness catLocationBusiness;

  @Mock
  CatItemRepository catItemRepository;

  @Mock
  TicketProvider ticketProvider;

  @Mock
  MrServiceRepository mrServiceRepository;

  @Before
  public void setUpUploadFolder() {
    ReflectionTestUtils.setField(mrITSoftScheduleBusiness, "tempFolder",
        "./test_junit");
  }

  @Test
  public void getListMrSchedule() {
    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    Datatable datatable = Mockito.spy(Datatable.class);
    when(mrITSoftScheduleRepository.getListMrSchedule(any())).thenReturn(datatable);
    Datatable result = mrITSoftScheduleBusiness.getListMrSchedule(mrITSoftScheduleDTO);
    assertEquals(datatable.getPages(), result.getPages());
  }

  @Test
  public void onUpdateSoft() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    dtoDevice.setMrSoft("1");
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setProcedureId("2");
//    mrScheduleITHisDTO.setMrDate("2");
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleITHisDTO);
    when(mrITSoftScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    MrITSoftProcedureDTO procedureItDTO = Mockito.spy(MrITSoftProcedureDTO.class);
    procedureItDTO.setTypeCr(2L);
    when(mrITSoftProcedureBusiness.getDetail(any())).thenReturn(procedureItDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("FALSE");
    when(mrITHisBusiness.insertList(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setMrConfirm("2");
    mrITSoftScheduleDTO.setScheduleId("1");
    mrITSoftScheduleBusiness.onUpdateSoft(mrITSoftScheduleDTO);
  }

  @Test
  public void onUpdateSoft1() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    dtoDevice.setMrSoft("1");
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setScheduleId(2L);
    mrScheduleITHisDTO.setProcedureId("2");
//    mrScheduleITHisDTO.setMrDate("2");
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleITHisDTO);
    when(mrITSoftScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    MrITSoftProcedureDTO procedureItDTO = Mockito.spy(MrITSoftProcedureDTO.class);
    procedureItDTO.setTypeCr(2L);
    when(mrITSoftProcedureBusiness.getDetail(any())).thenReturn(procedureItDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITHisBusiness.insertList(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTODetail = Mockito.spy(MrITSoftScheduleDTO.class);
    when(mrITSoftScheduleRepository.getDetail(any())).thenReturn(mrITSoftScheduleDTODetail);

    ResultInSideDto resultInSideDtodele = Mockito.spy(ResultInSideDto.class);
    resultInSideDtodele.setKey("FALSE");
    when(mrITSoftScheduleRepository.deleteListSchedule(any())).thenReturn(resultInSideDtodele);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setMrConfirm("2");
    mrITSoftScheduleDTO.setScheduleId("1");
    mrITSoftScheduleBusiness.onUpdateSoft(mrITSoftScheduleDTO);
  }

  @Test
  public void onUpdateSoft2() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    dtoDevice.setMrSoft("1");
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setScheduleId(2L);
    mrScheduleITHisDTO.setProcedureId("2");
    mrScheduleITHisDTO.setMrDate("27/03/2019 12:12:12");
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleITHisDTO);
    when(mrITSoftScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    MrITSoftProcedureDTO procedureItDTO = Mockito.spy(MrITSoftProcedureDTO.class);
    procedureItDTO.setTypeCr(2L);
    when(mrITSoftProcedureBusiness.getDetail(any())).thenReturn(procedureItDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITHisBusiness.insertList(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTODetail = Mockito.spy(MrITSoftScheduleDTO.class);
    when(mrITSoftScheduleRepository.getDetail(any())).thenReturn(mrITSoftScheduleDTODetail);

    when(mrITSoftScheduleRepository.deleteListSchedule(any())).thenReturn(resultInSideDto);

    ResultInSideDto resultInSideDtoup = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoup.setKey("FALSE");
    when(mrSynItSoftDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDtoup);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setMrConfirm("2");
    mrITSoftScheduleDTO.setScheduleId("1");
    mrITSoftScheduleBusiness.onUpdateSoft(mrITSoftScheduleDTO);
  }

  @Test
  public void onUpdateSoft3() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    dtoDevice.setMrSoft("1");
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setScheduleId(2L);
    mrScheduleITHisDTO.setProcedureId("2");
    mrScheduleITHisDTO.setMrDate("27/03/2019 12:12:12");
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleITHisDTO);
    when(mrITSoftScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    MrITSoftProcedureDTO procedureItDTO = Mockito.spy(MrITSoftProcedureDTO.class);
    procedureItDTO.setTypeCr(2L);
    when(mrITSoftProcedureBusiness.getDetail(any())).thenReturn(procedureItDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITHisBusiness.insertList(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTODetail = Mockito.spy(MrITSoftScheduleDTO.class);
    when(mrITSoftScheduleRepository.getDetail(any())).thenReturn(mrITSoftScheduleDTODetail);

    when(mrITSoftScheduleRepository.deleteListSchedule(any())).thenReturn(resultInSideDto);

    when(mrSynItSoftDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setMrConfirm("2");
    mrITSoftScheduleDTO.setScheduleId("1");
    mrITSoftScheduleBusiness.onUpdateSoft(mrITSoftScheduleDTO);
  }

  @Test
  public void onUpdateSoft4() throws Exception {
    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    dtoDevice.setMrSoft("1");
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    List<CrHisDTO> crHisDtos = Mockito.spy(ArrayList.class);
    crHisDtos.add(crHisDTO);
    when(mrScheduleTelRepository.checkExistCrId(anyString(), anyString())).thenReturn(crHisDtos);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("9");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("ERROR");
    when(mrITSoftScheduleRepository.updateMrSchedule(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTODetail = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTODetail.setCrId("1");
    when(mrITSoftScheduleRepository.getDetail(any())).thenReturn(mrITSoftScheduleDTODetail);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setMrConfirm("5");
    mrITSoftScheduleDTO.setScheduleId("2");
    mrITSoftScheduleDTO.setCrId("2");
    mrITSoftScheduleDTO.setMrId("2");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("test");
    mrITSoftScheduleBusiness.onUpdateSoft(mrITSoftScheduleDTO);
  }

  @Test
  public void onUpdateSoft5() throws Exception {
    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    dtoDevice.setMrSoft("1");
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    List<CrHisDTO> crHisDtos = Mockito.spy(ArrayList.class);
    crHisDtos.add(crHisDTO);
    when(mrScheduleTelRepository.checkExistCrId(anyString(), anyString())).thenReturn(crHisDtos);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("9");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITSoftScheduleRepository.updateMrSchedule(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTODetail = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTODetail.setCrId("1");
    when(mrITSoftScheduleRepository.getDetail(any())).thenReturn(mrITSoftScheduleDTODetail);

    ResultInSideDto resultInSideDtoSyn = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoSyn.setKey("ERROR");
    when(mrSynItSoftDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDtoSyn);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setMrConfirm("5");
    mrITSoftScheduleDTO.setScheduleId("2");
    mrITSoftScheduleDTO.setCrId("2");
    mrITSoftScheduleDTO.setMrId("2");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("test");
    mrITSoftScheduleBusiness.onUpdateSoft(mrITSoftScheduleDTO);
  }

  @Test
  public void onUpdateSoft6() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    dtoDevice.setMrSoft("1");
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    List<CrHisDTO> crHisDtos = Mockito.spy(ArrayList.class);
    crHisDtos.add(crHisDTO);
    when(mrScheduleTelRepository.checkExistCrId(anyString(), anyString())).thenReturn(crHisDtos);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("9");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITSoftScheduleRepository.updateMrSchedule(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTODetail = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTODetail.setCrId("1");
    when(mrITSoftScheduleRepository.getDetail(any())).thenReturn(mrITSoftScheduleDTODetail);

    when(mrSynItSoftDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDto);

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setProcedureId("2");
    mrScheduleITHisDTO.setMrDate("27/03/2019 12:12:12");
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(mrScheduleITHisDTO);
    when(mrITSoftScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    MrITSoftProcedureDTO procedureItDTO = Mockito.spy(MrITSoftProcedureDTO.class);
    procedureItDTO.setTypeCr(2L);
    when(mrITSoftProcedureBusiness.getDetail(any())).thenReturn(procedureItDTO);

    ResultInSideDto resultInSideDtoInsertList = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoInsertList.setKey("FALSE");
    when(mrITHisBusiness.insertList(any())).thenReturn(resultInSideDtoInsertList);

    MrITSoftScheduleDTO mrITSoftScheduleDTODetails = Mockito.spy(MrITSoftScheduleDTO.class);
    when(mrITSoftScheduleRepository.getDetail(1L)).thenReturn(mrITSoftScheduleDTODetails);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setMrConfirm("5");
    mrITSoftScheduleDTO.setScheduleId("2");
    mrITSoftScheduleDTO.setCrId("2");
    mrITSoftScheduleBusiness.onUpdateSoft(mrITSoftScheduleDTO);
  }

  @Test
  public void onUpdateSoft7() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    dtoDevice.setMrSoft("1");
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    List<CrHisDTO> crHisDtos = Mockito.spy(ArrayList.class);
    crHisDtos.add(crHisDTO);
    when(mrScheduleTelRepository.checkExistCrId(anyString(), anyString())).thenReturn(crHisDtos);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("9");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITSoftScheduleRepository.updateMrSchedule(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTODetail = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTODetail.setCrId("2");
    when(mrITSoftScheduleRepository.getDetail(any())).thenReturn(mrITSoftScheduleDTODetail);

    when(mrSynItSoftDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDto);

    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    when(mrITSoftScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    MrITSoftProcedureDTO procedureItDTO = Mockito.spy(MrITSoftProcedureDTO.class);
    procedureItDTO.setTypeCr(2L);
    when(mrITSoftProcedureBusiness.getDetail(any())).thenReturn(procedureItDTO);

    when(mrITHisBusiness.insertList(any())).thenReturn(resultInSideDto);

    ResultInSideDto resultInSideDtoUp = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoUp.setKey("ERROR");
    when(mrSynItSoftDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDtoUp);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setMrConfirm("5");
    mrITSoftScheduleDTO.setScheduleId("2");
    mrITSoftScheduleDTO.setCrId("2");
    mrITSoftScheduleBusiness.onUpdateSoft(mrITSoftScheduleDTO);
  }

  @Test
  public void onUpdateSoft8() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    dtoDevice.setMrSoft("1");
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    CrHisDTO crHisDTO = Mockito.spy(CrHisDTO.class);
    crHisDTO.setEarliestStartTime("27/03/2019 12:12:12");
    crHisDTO.setLatestEndTime("27/03/2019 12:12:12");
    List<CrHisDTO> crHisDtos = Mockito.spy(ArrayList.class);
    crHisDtos.add(crHisDTO);
    when(mrScheduleTelRepository.checkExistCrId(anyString(), anyString())).thenReturn(crHisDtos);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("9");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITSoftScheduleRepository.updateMrSchedule(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTODetail = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTODetail.setCrId("2");
    mrITSoftScheduleDTODetail.setMrId("2");
    when(mrITSoftScheduleRepository.getDetail(any())).thenReturn(mrITSoftScheduleDTODetail);

    when(mrSynItSoftDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDto);

    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    when(mrITSoftScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    MrITSoftProcedureDTO procedureItDTO = Mockito.spy(MrITSoftProcedureDTO.class);
    procedureItDTO.setTypeCr(2L);
    when(mrITSoftProcedureBusiness.getDetail(any())).thenReturn(procedureItDTO);

    when(mrITHisBusiness.insertList(any())).thenReturn(resultInSideDto);

    ResultInSideDto resultInSideDtoUp = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoUp.setKey("SUCCESS");
    when(mrSynItSoftDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDtoUp);

    MrInsideDTO mrDTO = Mockito.spy(MrInsideDTO.class);
    when(maintenanceMngtBusiness.findById(any())).thenReturn(mrDTO);

    ResultInSideDto resultInSideDtoUpdateMr = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoUpdateMr.setKey("FALSE");
    when(mrITSoftScheduleRepository.updateMrEarAndLastestTime(any()))
        .thenReturn(resultInSideDtoUpdateMr);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setMrConfirm("5");
    mrITSoftScheduleDTO.setScheduleId("2");
    mrITSoftScheduleDTO.setCrId("2");
    mrITSoftScheduleBusiness.onUpdateSoft(mrITSoftScheduleDTO);
  }

  @Test
  public void onUpdateSoft9() throws Exception {
    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    dtoDevice.setMrSoft("1");
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("8");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    MrITSoftScheduleDTO mrITSoftScheduleDTODetail = Mockito.spy(MrITSoftScheduleDTO.class);
    when(mrITSoftScheduleRepository.getDetail(any())).thenReturn(mrITSoftScheduleDTODetail);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("FALSE");
    when(mrITSoftScheduleRepository.updateMrSchedule(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setMrConfirm("5");
    mrITSoftScheduleDTO.setScheduleId("2");
    mrITSoftScheduleDTO.setCrId("2");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("test");
    mrITSoftScheduleBusiness.onUpdateSoft(mrITSoftScheduleDTO);
  }

  @Test
  public void onUpdateSoft10() throws Exception {
    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    dtoDevice.setMrSoft("1");
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("8");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    MrITSoftScheduleDTO mrITSoftScheduleDTODetail = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTODetail.setGroupCode("2");
    when(mrITSoftScheduleRepository.getDetail(any())).thenReturn(mrITSoftScheduleDTODetail);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITSoftScheduleRepository.updateMrSchedule(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTOs = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTOs.setScheduleId("2");
    List<MrITSoftScheduleDTO> lstMrScheduleITGroupCode = Mockito.spy(ArrayList.class);
    when(mrITSoftScheduleRepository.findListMrScheduleByGroupCode(any()))
        .thenReturn(lstMrScheduleITGroupCode);

    ResultInSideDto resultInSideDto1 = Mockito.spy(ResultInSideDto.class);
    resultInSideDto1.setKey("FALSE");
    when(mrSynItSoftDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setMrConfirm("5");
    mrITSoftScheduleDTO.setScheduleId("2");
    mrITSoftScheduleDTO.setCrId("2");
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("test");
    mrITSoftScheduleBusiness.onUpdateSoft(mrITSoftScheduleDTO);
  }

  @Test
  public void onUpdateSoft11() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("test");
    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    dtoDevice.setMrSoft("1");
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("8");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    MrITSoftScheduleDTO mrITSoftScheduleDTODetail = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTODetail.setGroupCode("2");
    when(mrITSoftScheduleRepository.getDetail(any())).thenReturn(mrITSoftScheduleDTODetail);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITSoftScheduleRepository.updateMrSchedule(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTOs = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTOs.setScheduleId("2");
    List<MrITSoftScheduleDTO> lstMrScheduleITGroupCode = Mockito.spy(ArrayList.class);
    when(mrITSoftScheduleRepository.findListMrScheduleByGroupCode(any()))
        .thenReturn(lstMrScheduleITGroupCode);

    ResultInSideDto resultInSideDtoUpdateSyn = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoUpdateSyn.setKey("FALSES");
    when(mrSynItSoftDevicesRepository.updateMrSynItDevice(any()))
        .thenReturn(resultInSideDtoUpdateSyn);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setMrConfirm("5");
    mrITSoftScheduleDTO.setScheduleId("2");
    mrITSoftScheduleDTO.setCrId("2");
    mrITSoftScheduleDTO.setNextDateModify("01/11/9999 00:00:00");
    mrITSoftScheduleDTO.setMrId("1");

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCreatedDate(new Date());
    crInsiteDTO.setEarliestStartTime(new Date());
    crInsiteDTO.setState("9");
    when(crServiceProxy.findCrByIdProxy(anyLong())).thenReturn(crInsiteDTO);

    List<CrHisDTO> crHisDtos = Mockito.spy(ArrayList.class);
    CrHisDTO hisDTO = Mockito.spy(CrHisDTO.class);
    crHisDtos.add(hisDTO);
    when(mrScheduleTelRepository.checkExistCrId(anyString(), anyString())).thenReturn(crHisDtos);

    MrITSoftScheduleDTO itSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    itSoftScheduleDTO.setCrId("1");
    itSoftScheduleDTO.setGroupCode("1");
    when(mrITSoftScheduleRepository.getDetail(anyLong())).thenReturn(itSoftScheduleDTO);

    MrInsideDTO mrDTO = Mockito.spy(MrInsideDTO.class);
    when(mrServiceRepository.findMrById(anyLong())).thenReturn(mrDTO);

    mrITSoftScheduleBusiness.onUpdateSoft(mrITSoftScheduleDTO);
  }

  @Test
  public void onUpdateSoft12() throws Exception {
    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    dtoDevice.setMrSoft("0");
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("8");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    MrITSoftScheduleDTO mrITSoftScheduleDTODetail = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTODetail.setGroupCode("2");
    when(mrITSoftScheduleRepository.getDetail(any())).thenReturn(mrITSoftScheduleDTODetail);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITSoftScheduleRepository.updateMrSchedule(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTOs = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTOs.setScheduleId("2");
    List<MrITSoftScheduleDTO> lstMrScheduleITGroupCode = Mockito.spy(ArrayList.class);
    when(mrITSoftScheduleRepository.findListMrScheduleByGroupCode(any()))
        .thenReturn(lstMrScheduleITGroupCode);

    when(mrSynItSoftDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setMrConfirm("5");
    mrITSoftScheduleDTO.setScheduleId("2");
    mrITSoftScheduleDTO.setCrId("2");
    mrITSoftScheduleBusiness.onUpdateSoft(mrITSoftScheduleDTO);
  }

  @Test
  public void onUpdateSoft13() throws Exception {
    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    dtoDevice.setMrSoft("1");
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("8");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    MrITSoftScheduleDTO mrITSoftScheduleDTODetail = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTODetail.setGroupCode("2");
    when(mrITSoftScheduleRepository.getDetail(any())).thenReturn(mrITSoftScheduleDTODetail);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITSoftScheduleRepository.updateMrSchedule(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTOs = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTOs.setScheduleId("2");
    List<MrITSoftScheduleDTO> lstMrScheduleITGroupCode = Mockito.spy(ArrayList.class);
    when(mrITSoftScheduleRepository.findListMrScheduleByGroupCode(any()))
        .thenReturn(lstMrScheduleITGroupCode);

    when(mrSynItSoftDevicesRepository.updateMrSynItDevice(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setMrConfirm("1");
    mrITSoftScheduleDTO.setScheduleId("2");
    mrITSoftScheduleDTO.setCrId("2");
    mrITSoftScheduleDTO.setNextDateModify("01/11/9999 11:11:11");

    List<MrScheduleITHisDTO> lst = Mockito.spy(ArrayList.class);
    MrScheduleITHisDTO mrScheduleITHisDTO = Mockito.spy(MrScheduleITHisDTO.class);
    mrScheduleITHisDTO.setMrDate("01/11/9999 11:11:11");
    mrScheduleITHisDTO.setProcedureId("1");
    mrScheduleITHisDTO.setCrId("1");
    mrScheduleITHisDTO.setScheduleId(1L);
    lst.add(mrScheduleITHisDTO);
    when(mrITSoftScheduleRepository.getListScheduleMoveToHis(any())).thenReturn(lst);

    MrITSoftProcedureDTO procedureItDTO = Mockito.spy(MrITSoftProcedureDTO.class);
    procedureItDTO.setArrayActionName("A");
    procedureItDTO.setTypeCr(0L);
    when(mrITSoftProcedureBusiness.getDetail(anyLong())).thenReturn(procedureItDTO);

    when(mrITHisBusiness.insertList(anyList())).thenReturn(resultInSideDto);
    when(mrITSoftScheduleRepository.deleteListSchedule(anyList())).thenReturn(resultInSideDto);
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("test");

    mrITSoftScheduleBusiness.onUpdateSoft(mrITSoftScheduleDTO);
  }

  @Test
  public void onUpdateSoft14() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.when(I18n.getLanguage(anyString())).thenReturn("test");
    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    dtoDevice.setMrSoft("1");
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    CrDTO crDTO = Mockito.spy(CrDTO.class);
    crDTO.setState("8");
    when(crServiceProxy.findCrById(any())).thenReturn(crDTO);

    MrITSoftScheduleDTO mrITSoftScheduleDTODetail = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTODetail.setGroupCode("2");
    when(mrITSoftScheduleRepository.getDetail(any())).thenReturn(mrITSoftScheduleDTODetail);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey("SUCCESS");
    when(mrITSoftScheduleRepository.updateMrSchedule(any())).thenReturn(resultInSideDto);

    MrITSoftScheduleDTO mrITSoftScheduleDTOs = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTOs.setScheduleId("2");
    List<MrITSoftScheduleDTO> lstMrScheduleITGroupCode = Mockito.spy(ArrayList.class);
    when(mrITSoftScheduleRepository.findListMrScheduleByGroupCode(any()))
        .thenReturn(lstMrScheduleITGroupCode);

    ResultInSideDto resultInSideDtoUpdateSyn = Mockito.spy(ResultInSideDto.class);
    resultInSideDtoUpdateSyn.setKey("FALSES");
    when(mrSynItSoftDevicesRepository.updateMrSynItDevice(any()))
        .thenReturn(resultInSideDtoUpdateSyn);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setMrConfirm("5");
    mrITSoftScheduleDTO.setScheduleId("2");
    mrITSoftScheduleDTO.setCrId("2");
    mrITSoftScheduleDTO.setNextDateModify("01/11/9999 00:00:00");
    mrITSoftScheduleDTO.setMrId("1");

    CrInsiteDTO crInsiteDTO = Mockito.spy(CrInsiteDTO.class);
    crInsiteDTO.setCreatedDate(new Date());
    crInsiteDTO.setEarliestStartTime(new Date());
    crInsiteDTO.setState("1");
    when(crServiceProxy.findCrByIdProxy(anyLong())).thenReturn(crInsiteDTO);

    List<CrHisDTO> crHisDtos = Mockito.spy(ArrayList.class);
    CrHisDTO hisDTO = Mockito.spy(CrHisDTO.class);
    crHisDtos.add(hisDTO);
    when(mrScheduleTelRepository.checkExistCrId(anyString(), anyString())).thenReturn(crHisDtos);

    MrITSoftScheduleDTO itSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    itSoftScheduleDTO.setCrId("1");
    itSoftScheduleDTO.setGroupCode("1");
    when(mrITSoftScheduleRepository.getDetail(anyLong())).thenReturn(itSoftScheduleDTO);

    MrInsideDTO mrDTO = Mockito.spy(MrInsideDTO.class);
    when(mrServiceRepository.findMrById(anyLong())).thenReturn(mrDTO);

    mrITSoftScheduleBusiness.onUpdateSoft(mrITSoftScheduleDTO);
  }

  @Test
  public void getDetail() {
    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    when(mrITSoftScheduleRepository.getDetail(any())).thenReturn(mrITSoftScheduleDTO);
    MrITSoftScheduleDTO result = mrITSoftScheduleBusiness.getDetail(1L);
    assertEquals(mrITSoftScheduleDTO, result);
  }

  @Test
  public void getListRegionByMrSynItDevices() {
    List<MrITSoftScheduleDTO> lst = Mockito.spy(ArrayList.class);
    when(mrITSoftScheduleRepository.getListRegionByMrSynItDevices(anyString())).thenReturn(lst);
    List<MrITSoftScheduleDTO> result = mrITSoftScheduleBusiness
        .getListRegionByMrSynItDevices("vietnam");
    assertEquals(lst.size(), result.size());
  }

  @Test
  public void deleteMrScheduleITSoft() {
    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    when(mrITSoftScheduleRepository.getDetail(any())).thenReturn(mrITSoftScheduleDTO);

    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    when(mrITSoftScheduleRepository.deleteListSchedule(any())).thenReturn(resultInSideDto);

    MrSynItDevicesDTO dtoDevice = Mockito.spy(MrSynItDevicesDTO.class);
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    mrITSoftScheduleBusiness.deleteMrScheduleITSoft(1L);
  }

  @Test
  public void exportData() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(CommonExport.class);
    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    List<MrITSoftScheduleDTO> mrITSoftScheduleDTOList = Mockito.spy(ArrayList.class);
    MrITSoftScheduleDTO itSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    itSoftScheduleDTO.setApproveStatus("1");
    itSoftScheduleDTO.setCycleType("1");
    itSoftScheduleDTO.setBdType("1");
    mrITSoftScheduleDTOList.add(itSoftScheduleDTO);
    mrITSoftScheduleDTOList.add(mrITSoftScheduleDTO);

    File fileExportSuccess = new File("./test_junit/test.txt");

    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    when(mrITSoftScheduleRepository.getDataExport(any())).thenReturn(mrITSoftScheduleDTOList);
    mrITSoftScheduleBusiness.exportData(mrITSoftScheduleDTO);
  }

  @Test
  public void getTemplate() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    ItemDataCRInside itemDataCRInside = Mockito.spy(ItemDataCRInside.class);
    itemDataCRInside.setValueStr(1L);
    List<ItemDataCRInside> lstMarketCode = Mockito.spy(ArrayList.class);
    lstMarketCode.add(itemDataCRInside);
    when(catLocationBusiness.getListLocationByLevelCBB(null, 1L, null)).thenReturn(lstMarketCode);

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setRegion("1");
    List<MrITSoftScheduleDTO> mrDeviceDTOList = Mockito.spy(ArrayList.class);
    mrDeviceDTOList.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleRepository.getListRegionByMrSynItDevices(any()))
        .thenReturn(mrDeviceDTOList);

    MrSynItDevicesDTO mrSynItDevicesDTO = Mockito.spy(MrSynItDevicesDTO.class);
    mrSynItDevicesDTO.setDeviceType("1");
    List<MrSynItDevicesDTO> lstDeviceType = Mockito.spy(ArrayList.class);
    lstDeviceType.add(mrSynItDevicesDTO);
    when(mrSynItSoftDevicesRepository.getListDeviceTypeByArrayCode(any()))
        .thenReturn(lstDeviceType);

    MrConfigDTO mrConfigDTO = Mockito.spy(MrConfigDTO.class);
    mrConfigDTO.setConfigName("1");
    List<MrConfigDTO> lstMrConfigDTO = Mockito.spy(ArrayList.class);
    lstMrConfigDTO.add(mrConfigDTO);
    when(mrScheduleTelRepository.getConfigByGroup(anyString())).thenReturn(lstMrConfigDTO);

    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.scheduleId"))
        .thenReturn("mtItSoftSchedule.scheduleId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nationName"))
        .thenReturn("mtItSoftSchedule.nationName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.region"))
        .thenReturn("mtItSoftSchedule.region");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.arrayCode"))
        .thenReturn("mtItSoftSchedule.arrayCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceType"))
        .thenReturn("mtItSoftSchedule.deviceType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceCode"))
        .thenReturn("mtItSoftSchedule.deviceCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceName"))
        .thenReturn("mtItSoftSchedule.deviceName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.ipNode"))
        .thenReturn("mtItSoftSchedule.ipNode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.vendor"))
        .thenReturn("mtItSoftSchedule.vendor");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.bdType"))
        .thenReturn("mtItSoftSchedule.bdType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.stationCode"))
        .thenReturn("mtItSoftSchedule.stationCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nodeAffected"))
        .thenReturn("mtItSoftSchedule.nodeAffected");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.groupCode"))
        .thenReturn("mtItSoftSchedule.groupCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDate"))
        .thenReturn("mtItSoftSchedule.nextDate");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.lastDate"))
        .thenReturn("mtItSoftSchedule.lastDate");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.mrId"))
        .thenReturn("mtItSoftSchedule.mrId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.crId"))
        .thenReturn("mtItSoftSchedule.crId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.wlgText"))
        .thenReturn("mtItSoftSchedule.wlgText");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.implementUnitName"))
        .thenReturn("mtItSoftSchedule.implementUnitName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.checkingUnitName"))
        .thenReturn("mtItSoftSchedule.checkingUnitName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.cycle"))
        .thenReturn("mtItSoftSchedule.cycle");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.cycleType"))
        .thenReturn("mtItSoftSchedule.cycleType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.note"))
        .thenReturn("mrDeviceSoft.mrTypeStr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.lastDateStr"))
        .thenReturn("mtItSoftSchedule.lastDateStr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDateStr"))
        .thenReturn("mtItSoftSchedule.nextDateStr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.ud"))
        .thenReturn("mtItSoftSchedule.ud");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.boUnit"))
        .thenReturn("mtItSoftSchedule.boUnit");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.approveStatus"))
        .thenReturn("mtItSoftSchedule.approveStatus");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDateModify"))
        .thenReturn("mtItSoftSchedule.nextDateModify");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.mrConfirmName"))
        .thenReturn("mtItSoftSchedule.mrConfirmName");
    //      </editor-fold>
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.title.hard")).thenReturn("test");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.region")).thenReturn("testt");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.arrNet")).thenReturn("testtt");

    CatItemDTO catItemDTO = Mockito.spy(CatItemDTO.class);
    catItemDTO.setItemName("1");
    List<CatItemDTO> lstArrayCode = Mockito.spy(ArrayList.class);
    lstArrayCode.add(catItemDTO);
    Datatable dataArray = Mockito.spy(Datatable.class);
    dataArray.setData(lstArrayCode);
    when(catItemRepository.getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME)).thenReturn(dataArray);

    mrITSoftScheduleBusiness.getTemplate("H");
  }

  @Test
  public void importData() {
    PowerMockito.mockStatic(I18n.class);
    MultipartFile multipartFile = null;
    mrITSoftScheduleBusiness.importData(multipartFile);
  }

  @Test
  public void importData1() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    String filePath = "/test_junit/test.txt";
    File fileImport = new File(filePath);
    when(FileUtils
        .saveTempFile(any(), any(), anyString())).thenReturn(filePath);

    List<Object[]> headerList = Mockito.spy(ArrayList.class);
    PowerMockito.when(
        CommonImport.getDataFromExcelFile(any(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
        .thenReturn(headerList);

    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    mrITSoftScheduleBusiness.importData(multipartFile);
  }

  @Test
  public void importData3() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    String filePath = "/test_junit/test.txt";
    File fileImport = new File(filePath);
    when(FileUtils
        .saveTempFile(any(), any(), anyString())).thenReturn(filePath);

    Object[] objecttest = new Object[]{};
    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 1502; i++) {
      dataImportList.add(objecttest);
    }
    PowerMockito
        .when(CommonImport
            .getDataFromExcelFileNew(fileImport,
                0,
                8,
                0,
                31,
                1500
            ))
        .thenReturn(dataImportList);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.scheduleId"))
        .thenReturn("mtItSoftSchedule.scheduleId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nationName"))
        .thenReturn("mtItSoftSchedule.nationName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.region"))
        .thenReturn("mtItSoftSchedule.region");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.arrayCode"))
        .thenReturn("mtItSoftSchedule.arrayCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceType"))
        .thenReturn("mtItSoftSchedule.deviceType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceCode"))
        .thenReturn("mtItSoftSchedule.deviceCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceName"))
        .thenReturn("mtItSoftSchedule.deviceName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.ipNode"))
        .thenReturn("mtItSoftSchedule.ipNode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.vendor"))
        .thenReturn("mtItSoftSchedule.vendor");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.bdType"))
        .thenReturn("mtItSoftSchedule.bdType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.stationCode"))
        .thenReturn("mtItSoftSchedule.stationCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nodeAffected"))
        .thenReturn("mtItSoftSchedule.nodeAffected");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.groupCode"))
        .thenReturn("mtItSoftSchedule.groupCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDate"))
        .thenReturn("mtItSoftSchedule.nextDate");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.lastDate"))
        .thenReturn("mtItSoftSchedule.lastDate");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.mrId"))
        .thenReturn("mtItSoftSchedule.mrId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.crId"))
        .thenReturn("mtItSoftSchedule.crId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.wlgText"))
        .thenReturn("mtItSoftSchedule.wlgText");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.descriptionCr"))
        .thenReturn("mtItSoftSchedule.descriptionCr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.implementUnitName"))
        .thenReturn("mtItSoftSchedule.implementUnitName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.checkingUnitName"))
        .thenReturn("mtItSoftSchedule.checkingUnitName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.cycle"))
        .thenReturn("mtItSoftSchedule.cycle");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.cycleType"))
        .thenReturn("mtItSoftSchedule.cycleType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.note"))
        .thenReturn("mrDeviceSoft.mrTypeStr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.lastDateStr"))
        .thenReturn("mtItSoftSchedule.lastDateStr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDateStr"))
        .thenReturn("mtItSoftSchedule.nextDateStr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.ud"))
        .thenReturn("mtItSoftSchedule.ud");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.boUnit"))
        .thenReturn("mtItSoftSchedule.boUnit");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.approveStatus"))
        .thenReturn("mtItSoftSchedule.approveStatus");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDateModify"))
        .thenReturn("mtItSoftSchedule.nextDateModify");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.mrConfirmName"))
        .thenReturn("mtItSoftSchedule.mrConfirmName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.db"))
        .thenReturn("mtItSoftSchedule.db");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.logMop"))
        .thenReturn("mtItSoftSchedule.logMop");
    //      </editor-fold>
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    Object[] objectHeader = new Object[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mtItSoftSchedule.scheduleId"),
        I18n.getLanguage("mtItSoftSchedule.nationName"),
        I18n.getLanguage("mtItSoftSchedule.region"),
        I18n.getLanguage("mtItSoftSchedule.arrayCode"),
        I18n.getLanguage("mtItSoftSchedule.deviceType"),
        I18n.getLanguage("mtItSoftSchedule.deviceCode"),
        I18n.getLanguage("mtItSoftSchedule.deviceName"),
        I18n.getLanguage("mtItSoftSchedule.ipNode"),
        I18n.getLanguage("mtItSoftSchedule.vendor"),
        I18n.getLanguage("mtItSoftSchedule.bdType"),
        I18n.getLanguage("mtItSoftSchedule.stationCode"),
        I18n.getLanguage("mtItSoftSchedule.nodeAffected"),
        I18n.getLanguage("mtItSoftSchedule.groupCode"),
        I18n.getLanguage("mtItSoftSchedule.nextDate"),
        I18n.getLanguage("mtItSoftSchedule.lastDate"),
        I18n.getLanguage("mtItSoftSchedule.nextDateModify"),
        I18n.getLanguage("mtItSoftSchedule.mrId"),
        I18n.getLanguage("mtItSoftSchedule.crId"),
        I18n.getLanguage("mtItSoftSchedule.wlgText"),
        I18n.getLanguage("mtItSoftSchedule.descriptionCr"),
        I18n.getLanguage("mtItSoftSchedule.implementUnitName"),
        I18n.getLanguage("mtItSoftSchedule.checkingUnitName"),
        I18n.getLanguage("mtItSoftSchedule.cycle"),
        I18n.getLanguage("mtItSoftSchedule.cycleType"),
        I18n.getLanguage("mtItSoftSchedule.note"),
        I18n.getLanguage("mtItSoftSchedule.ud"),
        I18n.getLanguage("mtItSoftSchedule.db"),
        I18n.getLanguage("mtItSoftSchedule.boUnit"),
        I18n.getLanguage("mtItSoftSchedule.approveStatus"),
        I18n.getLanguage("mtItSoftSchedule.mrConfirmName"),
        I18n.getLanguage("mtItSoftSchedule.logMop")
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
                31,
                1000
            ))
        .thenReturn(dataHeader);
    //      </editor-fold>

    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    mrITSoftScheduleBusiness.importData(multipartFile);
  }

  @Test
  public void importData4() throws Exception {
    PowerMockito.mockStatic(I18n.class);
    PowerMockito.mockStatic(FileUtils.class);
    PowerMockito.mockStatic(CommonImport.class);
    String filePath = "/test_junit/test.txt";
    File fileImport = new File(filePath);
    when(FileUtils
        .saveTempFile(any(), any(), anyString())).thenReturn(filePath);

    List<Object[]> dataImportList = Mockito.spy(ArrayList.class);
    for (int i = 0; i < 3; i++) {
      Object[] objectTest = new Object[32];
      for (int j = 0; j < objectTest.length; j++) {
        if (j == 16) {
          objectTest[j] = "27/03/2019";
        } else {
          objectTest[j] = "1";
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
                31,
                1500
            ))
        .thenReturn(dataImportList);
    //<editor-fold desc dunglv test defaultstate="collapsed">
    PowerMockito.when(I18n.getLanguage("common.STT"))
        .thenReturn("common.STT");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.scheduleId"))
        .thenReturn("mtItSoftSchedule.scheduleId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nationName"))
        .thenReturn("mtItSoftSchedule.nationName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.region"))
        .thenReturn("mtItSoftSchedule.region");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.arrayCode"))
        .thenReturn("mtItSoftSchedule.arrayCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceType"))
        .thenReturn("mtItSoftSchedule.deviceType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceCode"))
        .thenReturn("mtItSoftSchedule.deviceCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.deviceName"))
        .thenReturn("mtItSoftSchedule.deviceName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.ipNode"))
        .thenReturn("mtItSoftSchedule.ipNode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.vendor"))
        .thenReturn("mtItSoftSchedule.vendor");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.bdType"))
        .thenReturn("mtItSoftSchedule.bdType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.stationCode"))
        .thenReturn("mtItSoftSchedule.stationCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nodeAffected"))
        .thenReturn("mtItSoftSchedule.nodeAffected");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.groupCode"))
        .thenReturn("mtItSoftSchedule.groupCode");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDate"))
        .thenReturn("mtItSoftSchedule.nextDate");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.lastDate"))
        .thenReturn("mtItSoftSchedule.lastDate");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.mrId"))
        .thenReturn("mtItSoftSchedule.mrId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.crId"))
        .thenReturn("mtItSoftSchedule.crId");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.wlgText"))
        .thenReturn("mtItSoftSchedule.wlgText");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.descriptionCr"))
        .thenReturn("mtItSoftSchedule.descriptionCr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.implementUnitName"))
        .thenReturn("mtItSoftSchedule.implementUnitName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.checkingUnitName"))
        .thenReturn("mtItSoftSchedule.checkingUnitName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.cycle"))
        .thenReturn("mtItSoftSchedule.cycle");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.cycleType"))
        .thenReturn("mtItSoftSchedule.cycleType");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.note"))
        .thenReturn("mrDeviceSoft.mrTypeStr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.lastDateStr"))
        .thenReturn("mtItSoftSchedule.lastDateStr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDateStr"))
        .thenReturn("mtItSoftSchedule.nextDateStr");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.ud"))
        .thenReturn("mtItSoftSchedule.ud");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.boUnit"))
        .thenReturn("mtItSoftSchedule.boUnit");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.approveStatus"))
        .thenReturn("mtItSoftSchedule.approveStatus");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.nextDateModify"))
        .thenReturn("mtItSoftSchedule.nextDateModify");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.mrConfirmName"))
        .thenReturn("mtItSoftSchedule.mrConfirmName");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.db"))
        .thenReturn("mtItSoftSchedule.db");
    PowerMockito.when(I18n.getLanguage("mtItSoftSchedule.logMop"))
        .thenReturn("mtItSoftSchedule.logMop");
    //      </editor-fold>
    //<editor-fold desc dunglv test dataHeader defaultstate="collapsed">
    Object[] objectHeader = new Object[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mtItSoftSchedule.scheduleId"),
        I18n.getLanguage("mtItSoftSchedule.nationName"),
        I18n.getLanguage("mtItSoftSchedule.region"),
        I18n.getLanguage("mtItSoftSchedule.arrayCode"),
        I18n.getLanguage("mtItSoftSchedule.deviceType"),
        I18n.getLanguage("mtItSoftSchedule.deviceCode"),
        I18n.getLanguage("mtItSoftSchedule.deviceName"),
        I18n.getLanguage("mtItSoftSchedule.ipNode"),
        I18n.getLanguage("mtItSoftSchedule.vendor"),
        I18n.getLanguage("mtItSoftSchedule.bdType"),
        I18n.getLanguage("mtItSoftSchedule.stationCode"),
        I18n.getLanguage("mtItSoftSchedule.nodeAffected"),
        I18n.getLanguage("mtItSoftSchedule.groupCode"),
        I18n.getLanguage("mtItSoftSchedule.nextDate"),
        I18n.getLanguage("mtItSoftSchedule.lastDate"),
        I18n.getLanguage("mtItSoftSchedule.nextDateModify"),
        I18n.getLanguage("mtItSoftSchedule.mrId"),
        I18n.getLanguage("mtItSoftSchedule.crId"),
        I18n.getLanguage("mtItSoftSchedule.wlgText"),
        I18n.getLanguage("mtItSoftSchedule.descriptionCr"),
        I18n.getLanguage("mtItSoftSchedule.implementUnitName"),
        I18n.getLanguage("mtItSoftSchedule.checkingUnitName"),
        I18n.getLanguage("mtItSoftSchedule.cycle"),
        I18n.getLanguage("mtItSoftSchedule.cycleType"),
        I18n.getLanguage("mtItSoftSchedule.note"),
        I18n.getLanguage("mtItSoftSchedule.ud"),
        I18n.getLanguage("mtItSoftSchedule.db"),
        I18n.getLanguage("mtItSoftSchedule.boUnit"),
        I18n.getLanguage("mtItSoftSchedule.approveStatus"),
        I18n.getLanguage("mtItSoftSchedule.mrConfirmName"),
        I18n.getLanguage("mtItSoftSchedule.logMop")
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
                31,
                1000
            ))
        .thenReturn(dataHeader);
    //      </editor-fold>

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setScheduleId("2");
    List<MrITSoftScheduleDTO> results = Mockito.spy(ArrayList.class);
    results.add(mrITSoftScheduleDTO);
    when(mrITSoftScheduleRepository.getListMrSheduleITByIdForImport(any())).thenReturn(results);

    UserToken userToken = Mockito.spy(UserToken.class);
    userToken.setDeptId(1L);
    userToken.setUserID(999999l);
    userToken.setUserName("thanhlv12");
    PowerMockito.mockStatic(TicketProvider.class);
    PowerMockito.when(ticketProvider.getUserToken()).thenReturn(userToken);

    when(mrITSoftScheduleRepository.getDetail(any())).thenReturn(mrITSoftScheduleDTO);

    MrSynItDevicesDTO dtoDevice = null;
    when(mrSynItSoftDevicesRepository.findMrDeviceByObjectId(any())).thenReturn(dtoDevice);

    MultipartFile multipartFile = Mockito.spy(MultipartFile.class);
    mrITSoftScheduleBusiness.importData(multipartFile);
  }

  @Test
  public void getMapFromList() {
  }

  @Test
  public void validateFileSoftFormat() {
  }

  @Test
  public void exportFileEx() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    List<MrITSoftScheduleDTO> lstData = Mockito.spy(ArrayList.class);

    File fileExportSuccess = new File("./test_junit/test.txt");

    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    mrITSoftScheduleBusiness.exportFileEx(lstData, "RESULT_IMPORT", "H");
  }

  @Test
  public void exportFileEx1() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    List<MrITSoftScheduleDTO> lstData = Mockito.spy(ArrayList.class);

    File fileExportSuccess = new File("./test_junit/test.txt");

    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    mrITSoftScheduleBusiness.exportFileEx(lstData, "RESULT_IMPORTS", "H");
  }

  @Test
  public void exportFileEx2() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    List<MrITSoftScheduleDTO> lstData = Mockito.spy(ArrayList.class);

    File fileExportSuccess = new File("./test_junit/test.txt");

    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    mrITSoftScheduleBusiness.exportFileEx(lstData, "RESULT_IMPORT", "S");
  }

  @Test
  public void exportFileEx5() throws Exception {
    PowerMockito.mockStatic(CommonExport.class);
    PowerMockito.mockStatic(I18n.class);
    List<MrITSoftScheduleDTO> lstData = Mockito.spy(ArrayList.class);

    File fileExportSuccess = new File("./test_junit/test.txt");

    PowerMockito.when(CommonExport
        .exportExcel(anyString(), anyString(), anyList(), anyString(), any(String[].class)))
        .thenReturn(fileExportSuccess);
    mrITSoftScheduleBusiness.exportFileEx(lstData, "RESULT_IMPORTS", "S");
  }

  @Test
  public void readerHeaderSheet() {
    String[] lstStr = {"1", "2"};
    mrITSoftScheduleBusiness.readerHeaderSheet(lstStr);
  }

  @Test
  public void validateDuplicate() {
    PowerMockito.mockStatic(I18n.class);
    MrITSoftScheduleDTO dto = Mockito.spy(MrITSoftScheduleDTO.class);
    dto.setScheduleId("2");

    MrITSoftScheduleDTO mrITSoftScheduleDTO = Mockito.spy(MrITSoftScheduleDTO.class);
    mrITSoftScheduleDTO.setScheduleId("2");
    List<MrITSoftScheduleDTO> lstoExportDTOS = Mockito.spy(ArrayList.class);
    lstoExportDTOS.add(mrITSoftScheduleDTO);

    PowerMockito.when(I18n.getValidation("MrMaterialDTO.unique.file")).thenReturn("2");

    mrITSoftScheduleBusiness.validateDuplicate(dto, lstoExportDTOS);
  }
}
