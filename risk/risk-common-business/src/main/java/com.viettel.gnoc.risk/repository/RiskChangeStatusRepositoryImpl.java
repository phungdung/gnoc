package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.risk.dto.RiskChangeStatusDTO;
import com.viettel.gnoc.risk.model.RiskChangeStatusEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RiskChangeStatusRepositoryImpl extends BaseRepository implements
    RiskChangeStatusRepository {

  @Override
  public List<RiskChangeStatusDTO> onSearch(RiskChangeStatusDTO riskChangeStatusDTO, int start,
      int maxResult, String sortType, String sortField) {
    return onSearchEntity(RiskChangeStatusEntity.class, riskChangeStatusDTO, start, maxResult,
        sortType, sortField);
  }
}
