package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.risk.model.RiskSystemEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskSystemRepository {

  RiskSystemEntity getRiskSystemById(Long id);

}
