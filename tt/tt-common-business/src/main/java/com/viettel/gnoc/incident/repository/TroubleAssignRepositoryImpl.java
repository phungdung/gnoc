package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.incident.model.TroubleAssignEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TroubleAssignRepositoryImpl extends BaseRepository implements
    TroubleAssignRepository {

  @Override
  public TroubleAssignEntity findTroubleAssignById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(TroubleAssignEntity.class, id);
    }
    return null;
  }

  @Override
  public String updateTroubleAssign(TroubleAssignEntity troubleAssignEntity) {
    log.debug("Request to updateTroubleAssign: {}", troubleAssignEntity);
    getEntityManager().merge(troubleAssignEntity);
    return RESULT.SUCCESS;
  }
}
