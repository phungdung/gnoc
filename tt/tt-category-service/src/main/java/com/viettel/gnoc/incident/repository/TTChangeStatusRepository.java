package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TTChangeStatusDTO;

public interface TTChangeStatusRepository {

  Datatable getListTTChangeStatus(TTChangeStatusDTO ttChangeStatusDTO);

  TTChangeStatusDTO findTTChangeStatusDTO(TTChangeStatusDTO ttChangeStatusDTO);

  TTChangeStatusDTO findTTChangeStatusById(Long id);

  ResultInSideDto insertOrUpdateTTChangeStatus(TTChangeStatusDTO ttChangeStatusDTO);

  ResultInSideDto deleteTTChangeStatus(Long ttChangeStatusId);
}
