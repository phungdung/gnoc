package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRFlowExecuteDTO;
import java.io.File;
import java.util.List;

public interface SRFlowExecuteBusiness {

  Datatable getListSRFlowExecute(SRFlowExecuteDTO srFlowExecuteDTO);

  List<SRFlowExecuteDTO> getListSRFlowExecuteCBB(SRFlowExecuteDTO srFlowExecuteDTO);

  ResultInSideDto delete(String listflowId);

  SRFlowExecuteDTO getDetail(String listflowId);

  File exportSearchData(SRFlowExecuteDTO srFlowExecuteDTO) throws Exception;

}
