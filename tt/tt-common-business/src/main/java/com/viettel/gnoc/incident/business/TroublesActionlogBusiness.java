package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;

public interface TroublesActionlogBusiness {

  Datatable getListTroubleActionLogsDTO(TroublesInSideDTO troubleDTO);
}
