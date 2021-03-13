package com.viettel.gnoc.pt.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.pt.dto.CfgProblemTimeProcessDTO;
import com.viettel.gnoc.pt.model.CfgProblemTimeProcessEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgProblemTimeProcessRepository {

  CfgProblemTimeProcessEntity findById(long id);

  ResultInSideDto onInsert(CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO);

  ResultInSideDto onDeleteList(List<CfgProblemTimeProcessDTO> lstCfgProblemTimeProcess);

  ResultInSideDto onUpdateList(List<CfgProblemTimeProcessDTO> lstCfgProblemTimeProcess,
      CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO);

  String getSequence() throws Exception;

  Datatable getDataTableCfgProblemTimeProcessDTO(CfgProblemTimeProcessDTO dto);

  CfgProblemTimeProcessDTO getCfgProblemTimeProcessByDTO(CfgProblemTimeProcessDTO dto);

  List<CfgProblemTimeProcessDTO> getDataExportCfgProblemTimeProcessDTO(
      CfgProblemTimeProcessDTO dto);
}
