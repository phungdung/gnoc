package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.incident.dto.TroubleFileIbmDTO;
import com.viettel.gnoc.incident.dto.TroublesIbmDTO;
import com.viettel.gnoc.incident.model.TroubleFileIbmEntity;
import com.viettel.gnoc.incident.model.TroublesIbmEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TroublesIbmRepositoryImpl extends BaseRepository implements TroublesIbmRepository {

  @Override
  public Datatable getListTroublesIbmDTO(TroublesIbmDTO troublesIbmDTO) {
    BaseDto baseDto = sqlGetListTroublesIbmDTO(troublesIbmDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        troublesIbmDTO.getPage(), troublesIbmDTO.getPageSize(),
        TroublesIbmDTO.class, troublesIbmDTO.getSortName(),
        troublesIbmDTO.getSortType());
  }

  @Override
  public ResultInSideDto insertTroublesIbm(TroublesIbmDTO troublesIbmDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    TroublesIbmEntity troublesIbmEntity = getEntityManager().merge(troublesIbmDTO.toEntity());
    resultInSideDto.setId(troublesIbmEntity.getId());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  private BaseDto sqlGetListTroublesIbmDTO(TroublesIbmDTO troublesIbmDTO) {
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES_IBM,
        "get-List-Troubles-Ibm-DTO");
    Map<String, Object> parameters = new HashMap<>();
    if (troublesIbmDTO.getTroubleId() != null) {
      sql += " AND TROUBLE_ID = :troubleId";
      parameters.put("troubleId", troublesIbmDTO.getTroubleId());
    }
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public ResultInSideDto insertTroubleFileIbm(TroubleFileIbmDTO troubleFileIbmDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    TroubleFileIbmEntity troubleFileIbmEntity = getEntityManager()
        .merge(troubleFileIbmDTO.toEntity());
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(troubleFileIbmEntity.getFileId());
    return resultInSideDto;
  }

  @Override
  public TroublesIbmEntity findById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(TroublesIbmEntity.class, id);
    }
    return null;
  }

  @Override
  public List<GnocFileDto> getGnocFileIBM(Long troubleIbmId) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES_IBM, "get-List-File-Ibm-DTO");
    sql += " AND GF.BUSINESS_ID = :businessId ";
    Map<String, Object> params = new HashMap<>();
    params.put("businessId", troubleIbmId);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(GnocFileDto.class));
  }
}
