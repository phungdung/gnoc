package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrChecklistsBtsDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrChecklistBtsRepository {

  Datatable getListDataSearchWeb(MrChecklistsBtsDTO mrChecklistsBtsDTO);

  ResultInSideDto insertMrChecklistBts(MrChecklistsBtsDTO mrChecklistBtsDTO);

  ResultInSideDto updateMrChecklistBts(MrChecklistsBtsDTO mrChecklistBtsDTO);

  MrChecklistsBtsDTO findMrChecklistBtsByIdFromWeb(Long checklistId);

  MrChecklistsBtsDTO findMrChecklistBtsById(Long checklistId);

  ResultInSideDto deleteMrChecklistBts(Long checklistId);

  List<MrChecklistsBtsDTO> getListMrChecklistBtsExport(MrChecklistsBtsDTO mrChecklistBtsDTO);

  MrChecklistsBtsDTO checkMrChecklistBtsExit(MrChecklistsBtsDTO mrChecklistBtsDTO);
}
