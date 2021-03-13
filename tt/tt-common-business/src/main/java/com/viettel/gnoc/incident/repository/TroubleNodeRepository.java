package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.TroubleNodeEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface TroubleNodeRepository {

  ResultInSideDto insertTroubleNode(TroubleNodeEntity entity);

  Datatable getListTroubleNodeDTO(TroublesInSideDTO dto);

}
