package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;

public interface TroubleNodeBusiness {

  Datatable getListTroubleNodeDTO(TroublesInSideDTO dto);
}
