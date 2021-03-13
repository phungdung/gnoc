package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.risk.dto.RiskSystemFileDTO;
import com.viettel.gnoc.risk.model.RiskSystemFileEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RiskSystemFileRepositoryImpl extends BaseRepository implements
    RiskSystemFileRepository {

  @Override
  public ResultInSideDto insertRiskSystemFile(RiskSystemFileDTO riskSystemFileDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    RiskSystemFileEntity entity = getEntityManager().merge(riskSystemFileDTO.toEntity());
    resultInSideDto.setId(entity.getRiskSystemFileId());
    return resultInSideDto;
  }

  @Override
  public List<RiskSystemFileEntity> getListRiskSystemFileBySystemId(Long systemId) {
    return findByMultilParam(RiskSystemFileEntity.class, "systemId", systemId);
  }

  @Override
  public ResultInSideDto deleteRiskSystemFile(Long riskSystemFileId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    RiskSystemFileEntity entity = getEntityManager()
        .find(RiskSystemFileEntity.class, riskSystemFileId);
    getEntityManager().remove(entity);
    return resultInSideDto;
  }
}
