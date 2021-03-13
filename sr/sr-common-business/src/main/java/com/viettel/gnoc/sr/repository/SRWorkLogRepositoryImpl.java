package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRReasonRejectDTO;
import com.viettel.gnoc.sr.dto.SRWorkLogDTO;
import com.viettel.gnoc.sr.dto.SRWorklogTypeDTO;
import com.viettel.gnoc.sr.model.SRWorkLogEntity;
import com.viettel.gnoc.sr.model.SRWorklogTypeEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
@Transactional
public class SRWorkLogRepositoryImpl extends BaseRepository implements SRWorkLogRepository {

  @Override
  public List<SRWorkLogDTO> getListSRWorklogWithUnit(Long srId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_WORKLOG, "getListSRWorklogWithUnit");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(srId)) {
      sqlQuery += " AND T1.SR_ID = :srId ";
      parameters.put("srId", srId);
    }
    sqlQuery += " ORDER BY T1.CREATED_TIME DESC ";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRWorkLogDTO.class));
  }

  @Override
  public List<SRWorklogTypeDTO> getBySRStatus(SRWorklogTypeDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_WORKLOG, "getBySRStatus");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(dto.getSrStatus())) {
      sql += " AND instr(',' || SR_STATUS || ',', ',' || :srStatus || ',') > 0";
      parameters.put("srStatus", dto.getSrStatus());
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getStatus())) {
      sql += " AND STATUS = :status";
      parameters.put("status", dto.getStatus());
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRWorklogTypeDTO.class));
  }

  @Override
  public ResultInSideDto insertSRWorklog(SRWorkLogDTO srWorklogDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRWorkLogEntity srWorkLogEntity = getEntityManager().merge(srWorklogDTO.toEntity());
    resultInSideDto.setId(srWorkLogEntity.getWlId());
    return resultInSideDto;
  }

  @Override
  public List<SRWorklogTypeDTO> getListSRWorklogTypeDTO(SRWorklogTypeDTO dto, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(SRWorklogTypeEntity.class, dto, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public List<SRReasonRejectDTO> getReasonByStatus(Long wlTypeId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR_WORKLOG, "getReasonByStatus");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_wl_type_id", wlTypeId);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRReasonRejectDTO.class));
  }

  @Override
  public int checkInputWorklog(Long srId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR_WORKLOG, "checkInputWorklog");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("srId", srId);
    List<BaseDto> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(BaseDto.class));
    if (lst != null && !lst.isEmpty()) {
      return lst.get(0).getTotalRow();
    }
    return 0;
  }
}
