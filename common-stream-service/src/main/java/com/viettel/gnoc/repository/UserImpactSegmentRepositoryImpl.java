package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.dto.UserImpactSegmentDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class UserImpactSegmentRepositoryImpl extends BaseRepository implements
    UserImpactSegmentRepository {

  @Override
  public Datatable getListUserImpactSegmentOfCr(UserImpactSegmentDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        dto.getPage(), dto.getPageSize(),
        UserImpactSegmentDTO.class,
        dto.getSortName(), dto.getSortType());
  }

  @Override
  public List<ImpactSegmentDTO> getListImpactSegmentCBB() {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_USER_IMPACT_SEGMENT, "getListImpactSegmentCBB");
    Map<String, Object> parameter = new HashMap<>();
    parameter.put("p_leeLocale", I18n.getLocale());
    List<ImpactSegmentDTO> lstImpactSegment = getNamedParameterJdbcTemplate()
        .query(sql, parameter, BeanPropertyRowMapper.newInstance(ImpactSegmentDTO.class));
    return lstImpactSegment;
  }

  @Override
  public List<RolesDTO> getListRolesCBB() {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_USER_IMPACT_SEGMENT, "getListRolesCBB");
    Map<String, Object> parameter = new HashMap<>();
    parameter.put("p_leeLocale", I18n.getLocale());
    List<RolesDTO> lstRoles = getNamedParameterJdbcTemplate()
        .query(sql, parameter, BeanPropertyRowMapper.newInstance(RolesDTO.class));
    return lstRoles;
  }

  public BaseDto sqlSearch(UserImpactSegmentDTO userImpactSegmentDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_USER_IMPACT_SEGMENT, "getListUserImpactSegmentOfCr");
    Map<String, Object> parameters = new HashMap<>();
    if (userImpactSegmentDTO != null && userImpactSegmentDTO.getCreatedTimeFrom() != null
        && userImpactSegmentDTO.getCreatedTimeTo() != null) {
      sqlQuery = sqlQuery.replace("compareDate",
          " AND CREATED_DATE  >= to_date(" + "'" + userImpactSegmentDTO.getCreatedTimeFrom() + "'"
              + " , 'dd/MM/yyyy HH24:mi:ss') "
              + " AND CREATED_DATE       <= to_date( " + "'" + userImpactSegmentDTO
              .getCreatedTimeTo() + "'"
              + " , 'dd/MM/yyyy HH24:mi:ss')");
    } else {
      sqlQuery = sqlQuery.replace("compareDate", " ");
    }

    if (userImpactSegmentDTO != null && userImpactSegmentDTO.getRole() != null && !""
        .equals(userImpactSegmentDTO.getRole())) {
      String role = "";
      String[] lstRoleId = userImpactSegmentDTO.getRole().split(",");
      for (int i = 0; i < lstRoleId.length; i++) {
        if (i != lstRoleId.length - 1) {
          role += lstRoleId[i] + ",";
        } else {
          role += lstRoleId[i];
        }
      }
      sqlQuery = sqlQuery.replace("listRoleReplace", "WHERE rs.role_id IN (" + role + ")");
//      parameters.put("lstRole", lstRole);

    } else {
      sqlQuery = sqlQuery.replace("listRoleReplace", " ");
    }

    if (userImpactSegmentDTO != null && userImpactSegmentDTO.getImpactSegmentId() != null && !""
        .equals(userImpactSegmentDTO.getImpactSegmentId())) {
      String impactSegment = "";
      String[] lstImpactSegmentId = userImpactSegmentDTO.getImpactSegmentId().split(",");
      for (int i = 0; i < lstImpactSegmentId.length; i++) {
        if (i != lstImpactSegmentId.length - 1) {
          impactSegment += lstImpactSegmentId[i] + ",";
        } else {
          impactSegment += lstImpactSegmentId[i];
        }
      }
      sqlQuery = sqlQuery.replace("impactSegmentReplace",
          " WHERE uut.impact_segment_id IN  (" + impactSegment + ")");
    } else {
      sqlQuery = sqlQuery.replace("impactSegmentReplace", " ");
    }

    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
