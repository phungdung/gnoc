package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.risk.model.RiskSystemDetailEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskSystemDetailRepository {

  List<RiskSystemDetailEntity> getListEntityBySystemId(Long systemId);

}
