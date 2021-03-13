package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface RelatedTTRepository {

  Datatable getListRelatedTT(TroublesInSideDTO dto);

  Datatable getListRelatedTTByPopup(TroublesInSideDTO dto);

  Datatable getListRelatedTTByPopupAdd(TroublesInSideDTO dto);
}
