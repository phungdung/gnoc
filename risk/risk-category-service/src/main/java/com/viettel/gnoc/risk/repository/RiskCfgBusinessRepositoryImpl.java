package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.risk.dto.RiskCfgBusinessDTO;
import com.viettel.gnoc.risk.model.RiskCfgBusinessEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RiskCfgBusinessRepositoryImpl extends BaseRepository implements
    RiskCfgBusinessRepository {

  @Override
  public List<RiskCfgBusinessDTO> onSearch(RiskCfgBusinessDTO riskCfgBusinessDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(RiskCfgBusinessEntity.class, riskCfgBusinessDTO, rowStart, maxRow,
        sortType, sortFieldList);
  }

  @Override
  public ResultInSideDto deleteListRiskCfgBusiness(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<RiskCfgBusinessEntity> listEntity = findByMultilParam(RiskCfgBusinessEntity.class,
        "riskChangeStatusId", id);
    if (listEntity != null && listEntity.size() > 0) {
      for (RiskCfgBusinessEntity entity : listEntity) {
        getEntityManager().remove(entity);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertRiskCfgBusiness(RiskCfgBusinessDTO riskCfgBusinessDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().persist(riskCfgBusinessDTO.toEntity());
    return resultInSideDTO;
  }

  @Override
  public List<RiskCfgBusinessDTO> getListCfgByRiskChangeStatusId(Long riskChangeStatusId) {
    List<RiskCfgBusinessDTO> listDTO = new ArrayList<>();
    List<RiskCfgBusinessEntity> listEntity = findByMultilParam(RiskCfgBusinessEntity.class,
        "riskChangeStatusId", riskChangeStatusId);
    if (listEntity != null && listEntity.size() > 0) {
      for (RiskCfgBusinessEntity entity : listEntity) {
        listDTO.add(entity.toDTO());
      }
      return listDTO;
    }
    return null;
  }
}
