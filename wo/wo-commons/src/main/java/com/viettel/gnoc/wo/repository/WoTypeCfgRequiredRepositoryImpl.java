package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoTypeCfgRequiredDTO;
import com.viettel.gnoc.wo.model.WoTypeCfgRequiredEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoTypeCfgRequiredRepositoryImpl extends BaseRepository implements
    WoTypeCfgRequiredRepository {


  @Override
  public List<WoTypeCfgRequiredDTO> findAllByWoTypeID(Long woTypeId) {

    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE_CFG_REQUIRED, "wo-Type-CfgRequired");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(woTypeId)) {
      sqlQuery += " AND r.WO_TYPE_ID= :woTypeId ";
      parameters.put("woTypeId", woTypeId);
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(WoTypeCfgRequiredDTO.class));

  }

  @Override
  public ResultInSideDto add(WoTypeCfgRequiredDTO woTypeCfgRequiredDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoTypeCfgRequiredEntity woTypeCfgRequiredEntity = getEntityManager()
        .merge(woTypeCfgRequiredDTO.toEntity());
    resultInSideDto.setId(woTypeCfgRequiredEntity.getId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long woTypeCfgRequiredId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoTypeCfgRequiredEntity woTypeCfgRequiredEntity = getEntityManager()
        .find(WoTypeCfgRequiredEntity.class, woTypeCfgRequiredId);
    getEntityManager().remove(woTypeCfgRequiredEntity);
    return resultInSideDto;
  }

  @Override
  public List<WoTypeCfgRequiredDTO> getListWoTypeCfgRequiredByWoTypeId(Long woTypeId,
      String cfgCode) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE_CFG_REQUIRED,
            "get-list-wo-type-cfg-required-by-wo-type-id");
    Map<String, Object> parameters = new HashMap<>();

    parameters.put("woTypeId", woTypeId);

    if (!StringUtils.isStringNullOrEmpty(cfgCode)) {
      sqlQuery = sqlQuery + " and item_code = :itemCode";
      parameters.put("itemCode", cfgCode);
    }

    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(WoTypeCfgRequiredDTO.class));
  }
}
