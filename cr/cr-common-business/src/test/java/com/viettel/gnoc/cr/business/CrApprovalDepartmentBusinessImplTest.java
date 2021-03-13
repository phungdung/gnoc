package com.viettel.gnoc.cr.business;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.CR_ACTION;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentDTO;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.repository.CrApprovalDepartmentRepository;
import java.util.ArrayList;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest({CrApprovalDepartmentBusinessImpl.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*",
    "com.sun.org.apache.xalan.*"})
public class CrApprovalDepartmentBusinessImplTest {

  @InjectMocks
  CrApprovalDepartmentBusinessImpl crApprovalDepartmentBusiness;
  @Mock
  CrApprovalDepartmentRepository crApprovalDepartmentRepository;

  @Test
  public void searchSQL_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrApprovalDepartmentInsiteDTO dto = Mockito.spy(CrApprovalDepartmentInsiteDTO.class);
    CrProcessInsideDTO crProcessDTO = Mockito.spy(CrProcessInsideDTO.class);
    crProcessDTO.setCrProcessId(1L);
    dto.setCrProcessDTO(crProcessDTO);
    dto.setSortType(CR_ACTION.IS_EDIT);
    dto.setPage(1);
    dto.setPageSize(1);
    List<CrApprovalDepartmentInsiteDTO> lstAll = Mockito.spy(ArrayList.class);
    lstAll.add(dto);
    List<CrApprovalDepartmentInsiteDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(dto);
    PowerMockito.when(crApprovalDepartmentRepository.getApprovalDepartmentByCreator(any()))
        .thenReturn(lstAll);
    PowerMockito.when(crApprovalDepartmentRepository
        .getApprovalDepartmentByProcess(any())).thenReturn(lst);
    Datatable datatable = crApprovalDepartmentBusiness.searchSQL(dto);
    Assert.assertEquals(datatable.getPages(), 2L);
  }

  @Test
  public void searchSQL_02() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrApprovalDepartmentInsiteDTO dto = Mockito.spy(CrApprovalDepartmentInsiteDTO.class);
    CrProcessInsideDTO crProcessDTO = Mockito.spy(CrProcessInsideDTO.class);
    crProcessDTO.setCrProcessId(1L);
    dto.setCrProcessDTO(crProcessDTO);
    dto.setSortType(CR_ACTION.IS_EDIT);
    dto.setPage(1);
    dto.setPageSize(1);
    List<CrApprovalDepartmentInsiteDTO> lstAll = Mockito.spy(ArrayList.class);
    lstAll.add(dto);
    List<CrApprovalDepartmentInsiteDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(dto);
    PowerMockito.when(crApprovalDepartmentRepository.getApprovalDepartmentByCreator(any()))
        .thenReturn(null);
    PowerMockito.when(crApprovalDepartmentRepository
        .getApprovalDepartmentByProcess(any())).thenReturn(lst);
    Datatable datatable = crApprovalDepartmentBusiness.searchSQL(dto);
    Assert.assertEquals(datatable.getPages(), 1L);
  }

  @Test
  public void searchSQL_03() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrApprovalDepartmentInsiteDTO dto = Mockito.spy(CrApprovalDepartmentInsiteDTO.class);
    CrProcessInsideDTO crProcessDTO = Mockito.spy(CrProcessInsideDTO.class);
    crProcessDTO.setCrProcessId(1L);
    dto.setCrProcessDTO(crProcessDTO);
    dto.setSortType(CR_ACTION.IS_VIEW);
    dto.setPage(1);
    dto.setPageSize(1);
    List<CrApprovalDepartmentInsiteDTO> lstAll = Mockito.spy(ArrayList.class);
    lstAll.add(dto);
    List<CrApprovalDepartmentInsiteDTO> lst = Mockito.spy(ArrayList.class);
    lst.add(dto);
    PowerMockito.when(crApprovalDepartmentRepository.getApprovalDepartmentByCreator(any()))
        .thenReturn(null);
    PowerMockito.when(crApprovalDepartmentRepository
        .getApprovalDepartmentByProcess(any())).thenReturn(lst);
    Datatable datatable = crApprovalDepartmentBusiness.searchSQL(dto);
    Assert.assertEquals(datatable.getPages(), 0L);
  }

  @Test
  public void getApprovalDepartmentByProcess_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<CrApprovalDepartmentInsiteDTO> lst = Mockito.spy(ArrayList.class);
    PowerMockito.when(crApprovalDepartmentRepository
        .getApprovalDepartmentByProcess(any())).thenReturn(lst);
    List<CrApprovalDepartmentInsiteDTO> crApprovalDepartmentInsiteDTOS = crApprovalDepartmentBusiness
        .getApprovalDepartmentByProcess(1L);
    Assert.assertEquals(lst.size(), crApprovalDepartmentInsiteDTOS.size());
  }

  @Test
  public void search_01() {
    Logger logger = Mockito.spy(Logger.class);
    PowerMockito.doNothing().when(logger).debug(any());
    ResultInSideDto resultInSideDto = Mockito.spy(ResultInSideDto.class);
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<CrApprovalDepartmentInsiteDTO> crApprovalDepartmentInsiteDTOS = Mockito
        .spy(ArrayList.class);
    CrApprovalDepartmentInsiteDTO tDTO = Mockito.spy(CrApprovalDepartmentInsiteDTO.class);
    PowerMockito.when(
        crApprovalDepartmentRepository.search(any(), anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(crApprovalDepartmentInsiteDTOS);
    List<CrApprovalDepartmentInsiteDTO> crApprovalDepartmentInsiteDTOS1 = crApprovalDepartmentBusiness
        .search(tDTO, 1, 1, "asc", "ID");
    Assert.assertEquals(crApprovalDepartmentInsiteDTOS.size(),
        crApprovalDepartmentInsiteDTOS1.size());
  }

  @Test
  public void onSearch_01() {
    CrApprovalDepartmentDTO tDTO = Mockito.spy(CrApprovalDepartmentDTO.class);
    List<CrApprovalDepartmentDTO> lstExpect = Mockito.spy(ArrayList.class);
    lstExpect.add(tDTO);
    PowerMockito.when(crApprovalDepartmentRepository
        .onSearch(anyObject(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(lstExpect);
    List<CrApprovalDepartmentDTO> lstActuals = crApprovalDepartmentBusiness
        .onSearch(tDTO, 0, 1, "", "");
    Assert.assertEquals(lstExpect.size(), lstActuals.size());
  }
}
