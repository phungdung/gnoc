package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.TT_MASTER_CODE;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.incident.dto.TTCfgBusinessDTO;
import com.viettel.gnoc.incident.dto.TTChangeStatusDTO;
import com.viettel.gnoc.incident.model.TTCfgBusinessEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TTCfgBusinessRepositoryImpl extends BaseRepository implements
    TTCfgBusinessRepository {

  public BaseDto sqlSearchByOdChange(TTChangeStatusDTO ttChangeStatusDTO, boolean ws) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_TT_CFG_BUSINESS, "get-tt-cfg-bussiness-detail");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("changeStatusId", ttChangeStatusDTO.getId());
    parameters.put("cfgBusinessColumn", TT_MASTER_CODE.TT_CFG_BUSINESS_COLUMN);
    if (ttChangeStatusDTO != null) {
      if (ttChangeStatusDTO.getLstScopeOfUse() != null && !ttChangeStatusDTO.getLstScopeOfUse()
          .isEmpty()) {
        sqlQuery += " AND NVL(bu.SCOPE_OF_USE,1) in (:lstScopeOfUse) ";
        parameters.put("lstScopeOfUse", ttChangeStatusDTO.getLstScopeOfUse());
      }
    }
    if (ws) {
      sqlQuery += " AND NVL(bu.IS_VISIBLE,0) = 1 ";
    }
    sqlQuery += " ORDER BY bu.STT ASC,bu.IS_REQUIRED DESC,ci.item_value ASC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public List<TTCfgBusinessDTO> getListTTCfgBusiness(TTChangeStatusDTO ttChangeStatusDTO,
      boolean ws) {
    BaseDto baseOdCfgBusiness = sqlSearchByOdChange(ttChangeStatusDTO, ws);
    List<TTCfgBusinessDTO> list = getNamedParameterJdbcTemplate()
        .query(baseOdCfgBusiness.getSqlQuery(), baseOdCfgBusiness.getParameters(),
            BeanPropertyRowMapper.newInstance(TTCfgBusinessDTO.class));
    return list;
  }

  @Override
  public ResultInSideDto deleteListTTCfgBusiness(Long ttChangeStatusId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<TTCfgBusinessEntity> listEntity = findByMultilParam(TTCfgBusinessEntity.class,
        "ttChangeStatusId", ttChangeStatusId);
    if (listEntity != null && listEntity.size() > 0) {
      for (TTCfgBusinessEntity entity : listEntity) {
        getEntityManager().remove(entity);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertTTCfgBusiness(TTCfgBusinessDTO ttCfgBusinessDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().persist(ttCfgBusinessDTO.toEntity());
    return resultInSideDTO;
  }
}
