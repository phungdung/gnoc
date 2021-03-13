package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.MaterialThresDTO;
import com.viettel.gnoc.wo.dto.MaterialThresInsideDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MaterialThresRepositoryImpl extends BaseRepository implements MaterialThresRepository {

  @Override
  public List<MaterialThresDTO> getDataList(MaterialThresInsideDTO materialThresDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MATERIAL_THRES, "get-Data-List");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("copperCable", I18n.getLanguage("materialThres.copperCable"));
    parameters.put("coaxialCable", I18n.getLanguage("materialThres.coaxialCable"));
    parameters.put("categoryCode", Constants.CATEGORY.WO_ACTION_GROUP);
    if (materialThresDTO.getActionId() != null) {
      sql += " AND a.action_id = :actionId";
      parameters.put("actionId", materialThresDTO.getActionId());
    }
    if (materialThresDTO.getServiceId() != null) {
      sql += " AND a.service_id = :serviceId";
      parameters.put("serviceId", materialThresDTO.getServiceId());
    }
    if (materialThresDTO.getMaterialThresId() != null) {
      sql += " AND a.material_thres_id = :materialThresId";
      parameters.put("materialThresId", materialThresDTO.getMaterialThresId());
    }
    if (materialThresDTO.getMaterialId() != null) {
      sql += " AND a.material_id = :materialId";
      parameters.put("materialId", materialThresDTO.getMaterialId());
    }
    if (materialThresDTO.getInfraType() != null) {
      sql += " AND a.infra_type = :infraType";
      parameters.put("infraType", materialThresDTO.getInfraType());
    }
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(MaterialThresDTO.class));
  }

  @Override
  public List<MaterialThresInsideDTO> getListMaterialDTOByAction(
      MaterialThresInsideDTO materialThresDTO, boolean isEnable, String nationCode) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MATERIAL_THRES, "get-list-material-by-action");
    Map<String, Object> parameters = new HashMap<>();
    if (materialThresDTO.getActionId() != null && materialThresDTO.getActionId() != -1L) {
      sql += " and t.action_id = :actionId ";
      parameters.put("actionId", materialThresDTO.getActionId());
    }
    if (materialThresDTO.getServiceId() != null && materialThresDTO.getServiceId() != -1L) {
      sql += " and t.service_id = :serviceId ";
      parameters.put("serviceId", materialThresDTO.getServiceId());
    }
    if (materialThresDTO.getInfraType() != null && materialThresDTO.getInfraType() != -1L) {
      sql += " and t.infra_type = :infraType ";
      parameters.put("infraType", materialThresDTO.getInfraType());
    }

    if (isEnable) {
      sql += " and t.is_enable = 1 ";
      sql += " and m.is_enable = 1 ";
    }

    if (!StringUtils.isStringNullOrEmpty(nationCode)) {
      if ("VNM".equals(nationCode)) {
        sql += " and (m.nation_code = :nationCode or m.nation_code is null) ";
      } else {
        sql += "  and m.nation_code = :nationCode ";
      }
      parameters.put("nationCode", nationCode);
    }

    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(MaterialThresInsideDTO.class));
  }


  @Override
  public List<MaterialThresDTO> getListMaterialDTOByAction(/*Long woId, */Long actionId,
      Long serviceId, Long infraType, boolean isEnable, String nationCode, Long type) {
    List<MaterialThresDTO> list = new ArrayList<>();
    try {
      StringBuilder sql = new StringBuilder();
      sql.append(SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MATERIAL_THRES, "get-list-material-by-action"));
      Map<String, Object> parameters = new HashMap<>();

      if (type != null) { // la thiet bi
        sql.append(" and m.type = :type ");
        parameters.put("type", type);
      } else {
        sql.append(" and m.type is null ");
      }
      if (actionId != null && actionId != -1L) {
        sql.append(" and t.action_id = :actionId ");
        parameters.put("actionId", actionId);
      }
      if (serviceId != null && serviceId != -1L) {
        sql.append(" and t.service_id = :serviceId ");
        parameters.put("serviceId", serviceId);
      }
      if (infraType != null && infraType != -1L) {
        sql.append(" and t.infra_type = :infraType ");
        parameters.put("infraType", infraType);
      }

      if (isEnable) {
        sql.append(" and t.is_enable = 1 ");
        sql.append(" and m.is_enable = 1 ");
      }

      if (!StringUtils.isStringNullOrEmpty(nationCode)) {
        if ("VNM".equals(nationCode)) {
          sql.append(" and (m.nation_code = :nationCode or m.nation_code is null) ");
        } else {
          sql.append(" and m.nation_code = :nationCode ");
        }
        parameters.put("nationCode", nationCode);
      }

      list = getNamedParameterJdbcTemplate().query(sql.toString(), parameters,
          BeanPropertyRowMapper.newInstance(MaterialThresDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }
}
