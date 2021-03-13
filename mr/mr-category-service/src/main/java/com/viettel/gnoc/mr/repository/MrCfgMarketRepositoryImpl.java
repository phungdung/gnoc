package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCfgMarketDTO;
import com.viettel.gnoc.maintenance.model.MrCfgMarketEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrCfgMarketRepositoryImpl extends BaseRepository implements
    MrCfgMarketRepository {


  public List<MrCfgMarketDTO> getListCfgMarket(MrCfgMarketDTO mrCfgMarketDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CFG_MARKET,
            "getListCfgMarket");
    Map<String, Object> parameters = new HashMap<>();
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrCfgMarketDTO.class));
  }

  @Override
  public ResultInSideDto add(MrCfgMarketDTO mrCfgMarketDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrCfgMarketEntity mrCfgMarketEntity = getEntityManager().merge(mrCfgMarketDTO.toEntity());
    resultInSideDto.setId(mrCfgMarketEntity.getIdMarket());
    return resultInSideDto;
  }

  @Override
  public MrCfgMarketDTO findMrCfgMarketById(Long idMarket) {
    MrCfgMarketEntity entity = getEntityManager().find(MrCfgMarketEntity.class, idMarket);
    if (entity != null) {
      return entity.toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto updateCreateUser(String type, MrCfgMarketDTO mrCfgMarketDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    Map<String, Object> parameters = new HashMap<>();
    String sql = "";
    if (mrCfgMarketDTO != null) {
      sql = "UPDATE OPEN_PM.MR_CFG_MARKET ";
      if ("VT_SOFT".equalsIgnoreCase(type)) {
        sql += " SET CREATED_USER_SOFT = :user ";
      } else if ("VT_HARD".equalsIgnoreCase(type)) {
        sql += " SET CREATED_USER_HARD = :user ";
      } else if ("IT_SOFT".equalsIgnoreCase(type)) {
        sql += " SET CREATED_USER_IT_SOFT = :user ";
      } else if ("IT_HARD".equalsIgnoreCase(type)) {
        sql += " SET CREATED_USER_IT_HARD = :user ";
      }
      sql += ", UPDATED_TIME = sysdate, UPDATED_USER = :updateUser WHERE MARKET_CODE = :marketCode ";
      if (!StringUtils.isStringNullOrEmpty(mrCfgMarketDTO.getUpdatedUser())) {
        parameters.put("updateUser", mrCfgMarketDTO.getUpdatedUser());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCfgMarketDTO.getMarketCode())) {
        parameters.put("marketCode", mrCfgMarketDTO.getMarketCode());
      }
      if ("VT_SOFT".equalsIgnoreCase(type) && !StringUtils
          .isStringNullOrEmpty(mrCfgMarketDTO.getCreatedUserSoft())) {
        parameters.put("user", mrCfgMarketDTO.getCreatedUserSoft());
      } else if ("VT_HARD".equalsIgnoreCase(type) && !StringUtils
          .isStringNullOrEmpty(mrCfgMarketDTO.getCreatedUserHard())) {
        parameters.put("user", mrCfgMarketDTO.getCreatedUserHard());
      } else if ("IT_SOFT".equalsIgnoreCase(type) && !StringUtils
          .isStringNullOrEmpty(mrCfgMarketDTO.getCreatedUserItSoft())) {
        parameters.put("user", mrCfgMarketDTO.getCreatedUserItSoft());
      } else if ("IT_HARD".equalsIgnoreCase(type) && !StringUtils
          .isStringNullOrEmpty(mrCfgMarketDTO.getCreatedUserItHard())) {
        parameters.put("user", mrCfgMarketDTO.getCreatedUserItHard());
      }
    }
    int row = getNamedParameterJdbcTemplate().update(sql, parameters);
    resultInSideDto.setQuantitySucc(row);
    return resultInSideDto;
  }

}
