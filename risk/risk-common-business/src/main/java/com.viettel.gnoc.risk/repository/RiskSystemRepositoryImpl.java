package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.risk.model.RiskSystemEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RiskSystemRepositoryImpl extends BaseRepository implements RiskSystemRepository {

  @Override
  public RiskSystemEntity getRiskSystemById(Long id) {
    return getEntityManager().find(RiskSystemEntity.class, id);
  }
}
