package com.viettel.gnoc.pt.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.pt.dto.CfgProblemTimeProcessDTO;
import java.io.File;
import java.util.List;

public interface CfgProblemTimeProcessBusiness {

  CfgProblemTimeProcessDTO findById(long id);

  Datatable getListDataSearch(CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO);

  ResultInSideDto onInsert(CfgProblemTimeProcessDTO cfgTimeTroubleProcessListDTO) throws Exception;

  ResultInSideDto onDeleteList(List<CfgProblemTimeProcessDTO> lstCfgProblemTimeProcess);

  ResultInSideDto onUpdateList(List<CfgProblemTimeProcessDTO> lstCfgProblemTimeProcess,
      CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO) throws Exception;

  String getSequence() throws Exception;

  File exportData(CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO) throws Exception;

  CfgProblemTimeProcessDTO getCfgProblemTimeProcessByDTO(CfgProblemTimeProcessDTO dto);
}
