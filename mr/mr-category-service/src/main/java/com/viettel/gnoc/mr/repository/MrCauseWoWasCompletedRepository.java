package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCauseWoWasCompletedDTO;
import java.util.List;

public interface MrCauseWoWasCompletedRepository {

  ResultInSideDto insertOrUpdate(MrCauseWoWasCompletedDTO mrCauseWoWasCompletedDTO);

  ResultInSideDto checkExisted(String reasonCode, String reasonType, String id);

  Datatable onSearch(MrCauseWoWasCompletedDTO dto);

  MrCauseWoWasCompletedDTO findById(Long id);

  List<MrCauseWoWasCompletedDTO> onSearchExport(MrCauseWoWasCompletedDTO dto);

  ResultInSideDto delete(Long id);

  List<MrCauseWoWasCompletedDTO> getReasonWO(String reasonTypeId);
}
