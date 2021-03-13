package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;

public interface TroublesRelatedBusiness {

  Datatable getListRelatedTT(TroublesInSideDTO dto);

  Datatable getListRelatedTTByPopup(TroublesInSideDTO dto);

  Datatable getListRelatedTTByPopupAdd(TroublesInSideDTO dto);
}
