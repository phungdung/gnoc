package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoHistoryDTO;
import com.viettel.gnoc.wo.dto.WoHistoryInsideDTO;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoHistoryRepositoryImpl extends BaseRepository implements WoHistoryRepository {

  @Autowired
  UserRepository userRepository;

  @Override
  public ResultInSideDto insertWoHistory(WoHistoryInsideDTO woHistoryInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().persist(woHistoryInsideDTO.toEntity());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateWoHistory(WoHistoryInsideDTO woHistoryInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().merge(woHistoryInsideDTO.toEntity());
    resultInSideDto.setId(woHistoryInsideDTO.getWoHistoryId());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public Datatable getListWoHistoryByWoId(WoHistoryInsideDTO woHistoryInsideDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_HISTORY, "get-List-Wo-History-By-Wo-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("offset", woHistoryInsideDTO.getOffset());
    parameters.put("woId", woHistoryInsideDTO.getWoId());
    return getListDataTableBySqlQuery(sql, parameters, woHistoryInsideDTO.getPage(),
        woHistoryInsideDTO.getPageSize(), WoHistoryInsideDTO.class,
        woHistoryInsideDTO.getSortName(),
        woHistoryInsideDTO.getSortType());
  }

  @Override
  public List<WoHistoryInsideDTO> getListWoHistoryDTO(WoHistoryInsideDTO woHistoryInsideDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_HISTORY, "get-List-Wo-History-DTO");
    Map<String, Object> parameters = new HashMap<>();
    if (woHistoryInsideDTO.getWoId() != null) {
      sql += " AND h.WO_ID = :woId";
      parameters.put("woId", woHistoryInsideDTO.getWoId());
    }
    if (woHistoryInsideDTO.getNewStatus() != null) {
      sql += " AND h.NEW_STATUS = :newStatus";
      parameters.put("newStatus", woHistoryInsideDTO.getNewStatus());
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoHistoryInsideDTO.class));
  }

  @Override
  public List<WoHistoryInsideDTO> getListDataByWoId(Long woId, Double offsetFromUser) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_HISTORY, "get-List-Wo-History-By-Wo-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("offset", offsetFromUser);
    parameters.put("woId", woId);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoHistoryInsideDTO.class));
  }

  @Override
  public List<WoHistoryDTO> getListWoHistoryBySystem(String username, String woId, String system,
      String systemId//
      , String startDate, String endDate) throws ParseException {
    StringBuilder sql = new StringBuilder(SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_HISTORY, "get-List-Wo-History-By-System"));
    Map<String, Object> params = new HashMap<>();
    Double offset = 0D;
    if (!StringUtils.isStringNullOrEmpty(username)) {
      offset = userRepository.getOffsetFromUser(username);
    }
    if (!StringUtils.isStringNullOrEmpty(woId)) {
      sql.append(" AND wo_id = :wo_id ");
      params.put("wo_id", woId);
    }

    if (!StringUtils.isStringNullOrEmpty(system)) {
      sql.append(" AND wo_id in( select wo_id from wo where wo_system = :system ) ");
      params.put("system", system);
    }

    if (!StringUtils.isStringNullOrEmpty(systemId)) {
      sql.append(" AND wo_id in( select wo_id from wo where wo_system_id = :system_id ) ");
      params.put("system_id", systemId);
    }

    if (!StringUtils.isStringNullOrEmpty(startDate)) {
      sql.append(" AND updateTime >= to_date(:updateTime, 'dd/MM/yyyy HH24:MI:ss') ");
      params.put("updateTime", DateTimeUtils.convertToDate(startDate, null, offset, true));
    }
    if (!StringUtils.isStringNullOrEmpty(endDate)) {
      sql.append(" AND endDate <= to_date(:endDate, 'dd/MM/yyyy HH24:MI:ss') ");
      params.put("endDate", DateTimeUtils.convertToDate(endDate, null, offset, true));
    }
    List<WoHistoryDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(WoHistoryDTO.class));
    if (list == null) {
      return new ArrayList<>();
    }
    return list;
  }

}
