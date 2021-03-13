package com.viettel.gnoc.kedb.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.kedb.dto.KedbActionLogsDTO;
import java.util.List;

public interface KedbActionLogsRepository {

  List<KedbActionLogsDTO> getListKedbActionLogsDTO(KedbActionLogsDTO kedbActionLogsDTO);

  List<KedbActionLogsDTO> getListKedbActionLogsByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<String> getSequenseKedbActionLogs(String seqName, int... size);

  String updateKedbActionLogs(KedbActionLogsDTO kedbActionLogsDTO);

  ResultInSideDto insertKedbActionLogs(KedbActionLogsDTO kedbActionLogsDTO);

  String insertOrUpdateListKedbActionLogs(List<KedbActionLogsDTO> kedbActionLogsDTO);

  KedbActionLogsDTO findKedbActionLogsById(Long id);

  String deleteKedbActionLogs(Long id);

  String deleteListKedbActionLogs(List<KedbActionLogsDTO> kedbActionLogsListDTO);

  Datatable onSearchKedbActionLogs(KedbActionLogsDTO dto);
}
