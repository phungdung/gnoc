package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CompCauseDTO;
import com.viettel.gnoc.commons.model.CompCauseEntity;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CompCauseRepositoryImpl extends BaseRepository implements CompCauseRepository {

  @Override
  public List<CompCauseDTO> getComCauseList(Long serviceTypeId, List<Long> ccGroupId,
      Long parentId, Integer levelId, String lineType, Long cfgType, String nationCode,
      Boolean isEnable)
      throws Exception {
    Map<String, Object> param = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-compcause-list");
    sql += " AND ISACTIVE = 1";
    if (serviceTypeId != null && !serviceTypeId.equals(-1L)) {
      sql += " AND comp_cause_id in (select comp_cause_id "
          + "  from common_gnoc.comp_cause_ser_type_map where service_type_id = :service_type_id )";
      param.put("service_type_id", serviceTypeId);
    }
    if (ccGroupId != null && ccGroupId.size() > 0) {
      sql += " and COMP_CAUSE_ID in ("
          + " select distinct(comp_cause_id) from common_gnoc.comp_group_cause_map where group_id in (:group_id))";
      param.put("group_id", ccGroupId);
    }

    if (cfgType != null && cfgType.equals(1L)) {//cho KV
      sql += " AND (CFG_TYPE is null or CFG_TYPE = 1 or CFG_TYPE = 3) ";
    } else if (cfgType != null && cfgType.equals(2L)) {//cho tinh
      sql += " AND (CFG_TYPE is null or CFG_TYPE = 2 or CFG_TYPE = 3) ";
    }

    if (levelId != null && !levelId.equals(-1)) {
      sql += " AND LEVEL_ID = :levelId";
      param.put("levelId", levelId);
    }
    if (parentId != null && !parentId.equals(-1L)) {
      sql += " AND PARENT_ID = :parentId";
      param.put("parentId", parentId);
    }
    if (lineType != null && !"".equals(lineType)) {
      if ("N/A".equalsIgnoreCase(lineType)) {
        lineType = "ALL";
      }
      sql += " AND LINE_TYPE = :lineType";
      param.put("lineType", lineType);
    }

    if (nationCode != null && !"VNM".equals(nationCode)) {
      if (nationCode != null && !"".equals(nationCode.trim())) {
        param.put("nationCode", nationCode);
      }
    } else {
      sql += " AND (NATION_CODE is null or NATION_CODE = 'VNM')";
    }

    List<CompCauseDTO> compCauseDTOS = getNamedParameterJdbcTemplate().query(sql, param,
        BeanPropertyRowMapper.newInstance(CompCauseDTO.class));

    return compCauseDTOS;
  }

  @Override
  public Map<String, Object> mapLanguageExchange(String leelocale, String mySystem,
      String myBussiness) {
    return getSqlLanguageExchange(mySystem, myBussiness, leelocale);
  }

  @Override
  public CompCauseDTO findCompCauseById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(CompCauseEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public CompCauseDTO getCompCauseById(Long id) {
    String sql =
        " select a.comp_cause_id compCauseId,a.name,a.parent_id parentId, a.level_id levelId from common_gnoc.comp_cause a "
            + " where a.comp_cause_id = :id";
    Map<String, Object> param = new HashMap<>();
    param.put("id", id);
    List<CompCauseDTO> lstCompCauseDTOS = getNamedParameterJdbcTemplate().query(sql, param,
        BeanPropertyRowMapper.newInstance(CompCauseDTO.class));
    if (lstCompCauseDTOS != null && !lstCompCauseDTOS.isEmpty()) {
      return lstCompCauseDTOS.get(0);
    }
    return null;
  }
}
