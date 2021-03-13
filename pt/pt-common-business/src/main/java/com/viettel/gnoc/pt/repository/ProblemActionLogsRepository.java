package com.viettel.gnoc.pt.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.pt.dto.ProblemActionLogsDTO;
import java.util.List;

public interface ProblemActionLogsRepository {

  List<ProblemActionLogsDTO> getListProblemActionLogsByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<ProblemActionLogsDTO> getListProblemActionLogsDTO(ProblemActionLogsDTO problemActionLogsDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  String insertOrUpdateListProblemActionLogs(List<ProblemActionLogsDTO> problemActionLogsDTO);

  ResultInSideDto insertProblemActionLogs(ProblemActionLogsDTO problemActionLogsDTO);

  ProblemActionLogsDTO findProblemActionLogsById(Long id);

  List<String> getSequenseProblemActionLogs(String seqName, int... size);

  String updateProblemActionLogs(ProblemActionLogsDTO problemActionLogsDTO);

  String deleteListProblemActionLogs(List<ProblemActionLogsDTO> problemActionLogsListDTO);

  String deleteProblemActionLogs(Long id);

  Datatable onSearchProblemActionLogsDTO(ProblemActionLogsDTO dto);
}
