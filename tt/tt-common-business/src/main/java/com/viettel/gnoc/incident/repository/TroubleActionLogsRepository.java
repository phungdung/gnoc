package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.ActionInfoDTO;
import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import com.viettel.gnoc.incident.model.TroubleActionLogsEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TroubleActionLogsRepository {

  ResultInSideDto insertTroubleActionLogs(TroubleActionLogsEntity entity);

  List<TroubleActionLogsDTO> getListTroubleActionLogsDTO(TroubleActionLogsDTO dto, int start,
      int maxResult, String sortType, String sortField);

  ResultInSideDto delete(Long id);

  ResultInSideDto deleteTroubleActionLogsByTroubleId(Long troubleId);

  List<String> onSearchActionInfo(ActionInfoDTO dto);
}
