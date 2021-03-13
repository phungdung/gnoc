package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.risk.dto.RiskFileDTO;
import com.viettel.gnoc.risk.model.RiskFileEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RiskFileRepositoryImpl extends BaseRepository implements RiskFileRepository {

  @Override
  public ResultInSideDto insertRiskFile(RiskFileDTO riskFileDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    RiskFileEntity entity = getEntityManager().merge(riskFileDTO.toEntity());
    resultInSideDto.setId(entity.getRiskFileId());
    return resultInSideDto;
  }

  @Override
  public List<RiskFileEntity> getListRiskFileByRiskId(Long riskId) {
    return findByMultilParam(RiskFileEntity.class, "riskId", riskId);
  }

  @Override
  public ResultInSideDto deleteRiskFile(Long riskFileId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    RiskFileEntity entity = getEntityManager().find(RiskFileEntity.class, riskFileId);
    getEntityManager().remove(entity);
    return resultInSideDto;
  }
}
