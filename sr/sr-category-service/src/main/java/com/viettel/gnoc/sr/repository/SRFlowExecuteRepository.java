package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRFlowExecuteDTO;
import java.util.List;

public interface SRFlowExecuteRepository {

  Datatable getListSRFlowExecute(SRFlowExecuteDTO srFlowExecuteDTO);

  List<SRFlowExecuteDTO> onSearchExport(SRFlowExecuteDTO srFlowExecuteDTO);

  List<SRFlowExecuteDTO> getListSRFlowExecuteCBB(SRFlowExecuteDTO srFlowExecuteDTO);

  ResultInSideDto insertOrUpdate(SRFlowExecuteDTO srFlowExecuteDTO);

  ResultInSideDto delete(Long flowId);

  SRFlowExecuteDTO getDetail(Long flowId);

  List<SRFlowExecuteDTO> isFlowUsingByCatalog(Long flowId);

  List<SRFlowExecuteDTO> isFlowUsingByCatalogTable(String listflowId);

  Boolean isDuplicateFlowName(String flowName);

  List<SRFlowExecuteDTO> getListSRFlowAlreadyExist(SRFlowExecuteDTO srFlowExecuteDTO);

}
