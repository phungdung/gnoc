package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.DataItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.CfgFtOnTimeDTO;
import com.viettel.gnoc.wo.dto.CfgWoHighTempDTO;
import com.viettel.gnoc.wo.dto.WoPriorityDTO;
import com.viettel.gnoc.wo.model.CfgFtOnTimeEntity;
import com.viettel.gnoc.wo.model.CfgWoHighTempEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgWoHighTempRepositoryImpl extends BaseRepository implements
    CfgWoHighTempRepository {

  @Override
  public Datatable onSearch(CfgWoHighTempDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        dto.getPage(), dto.getPageSize(),
        CfgWoHighTempDTO.class,
        dto.getSortName(), dto.getSortType());
  }

  public BaseDto sqlSearch(CfgWoHighTempDTO dto) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_WO_HIGH_TEMP,
            "on-search-cfg-wo-high-temp");
    Map<String, Object> parameters = new HashMap<>();
    if (dto != null) {
      if (dto.getReasonLv1Id() != null) {
        sqlQuery += " AND T1.REASON_LV1_ID = :reasonLv1Id ";
        parameters.put("reasonLv1Id", dto.getReasonLv1Id());
      }
      if (dto.getReasonLv2Id() != null) {
        sqlQuery += " AND T1.REASON_LV2_ID = :reasonLv2Id ";
        parameters.put("reasonLv2Id", dto.getReasonLv2Id());
      }
      if (dto.getActionId() != null) {
        sqlQuery += " AND T1.ACTION_ID = :actionId ";
        parameters.put("actionId", dto.getActionId());
      }
      if (dto.getActionNameId() != null) {
        sqlQuery += " AND T1.ACTION_NAME_ID = :actionNameId ";
        parameters.put("actionNameId", dto.getActionNameId());
      }
      if (dto.getPriorityId() != null) {
        sqlQuery += " AND T1.PRIORITY_ID = :priorityId ";
        parameters.put("priorityId", dto.getPriorityId());
      }
      if (dto.getStatus() != null) {
        if (dto.getStatus() == 1L) {
          sqlQuery += " AND T1.STATUS = :status ";
          parameters.put("status", dto.getStatus());
        } else {
          sqlQuery += " AND (T1.STATUS = 0 OR T1.STATUS IS NULL) ";
        }
      }
    }
    sqlQuery += " ORDER BY T1.ID DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public ResultInSideDto insertOrUpdate(CfgWoHighTempDTO dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgWoHighTempEntity entity = getEntityManager().merge(dto.toEntity());
    resultInSideDto.setId(entity.getId());
    return resultInSideDto;
  }

  @Override
  public CfgWoHighTempDTO findById(Long id) {
    if (id != null) {
      return getEntityManager().find(CfgWoHighTempEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgWoHighTempEntity entity = getEntityManager()
        .find(CfgWoHighTempEntity.class, id);
    if (entity != null) {
      getEntityManager().remove(entity);
    }
    return resultInSideDto;
  }

  @Override
  public List<CfgWoHighTempDTO> onSearchExport(CfgWoHighTempDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(CfgWoHighTempDTO.class));
  }

  @Override
  public boolean checkExisted(Long reasonLv1, Long reasonLv2, Long actionId, String id) {
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append(
        "select id from WFM.CFG_WO_HIGH_TEMP WHERE 1=1 AND REASON_LV1_ID = :reasonLv1 AND REASON_LV2_ID = :reasonLv2 AND ACTION_ID = :actionId ");
    parameters.put("reasonLv1", reasonLv1);
    parameters.put("reasonLv2", reasonLv2);
    parameters.put("actionId", actionId);
    List<CfgWoHighTempDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CfgWoHighTempDTO.class));
    if (!StringUtils.isStringNullOrEmpty(id)) {
      for (int i = 0; i < list.size(); i++) {
        if (list.get(i).getId().toString().equals(id)) {
          list.remove(i);
          break;
        }
      }
    }
    if (list != null && list.size() > 0) {
      return true;
    }
    return false;
  }

  @Override
  public List<DataItemDTO> getListPriority() {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_WO_HIGH_TEMP, "get-priority-wo");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("leeLocale", I18n.getLocale());
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(DataItemDTO.class));
  }
}
