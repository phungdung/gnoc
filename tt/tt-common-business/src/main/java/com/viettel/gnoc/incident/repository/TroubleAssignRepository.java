package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.incident.model.TroubleAssignEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface TroubleAssignRepository {

  TroubleAssignEntity findTroubleAssignById(Long id);

  String updateTroubleAssign(TroubleAssignEntity troubleAssignEntity);
}
