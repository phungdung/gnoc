package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.risk.model.RiskSystemDetailEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RiskSystemDetailRepositoryImpl extends BaseRepository implements
    RiskSystemDetailRepository {

  @Override
  public List<RiskSystemDetailEntity> getListEntityBySystemId(Long systemId) {
    return (List<RiskSystemDetailEntity>) findByMultilParam(
        RiskSystemDetailEntity.class, "systemId", systemId);
  }
}
