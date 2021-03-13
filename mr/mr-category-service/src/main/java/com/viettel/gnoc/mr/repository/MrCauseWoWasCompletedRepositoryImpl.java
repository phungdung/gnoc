package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.MR_ITEM_NAME;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCauseWoWasCompletedDTO;
import com.viettel.gnoc.maintenance.model.MrCauseWoWasCompletedEntity;
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
public class MrCauseWoWasCompletedRepositoryImpl extends BaseRepository implements
    MrCauseWoWasCompletedRepository {

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UserRepository userRepository;

  @Override
  public Datatable onSearch(MrCauseWoWasCompletedDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        dto.getPage(), dto.getPageSize(),
        MrCauseWoWasCompletedDTO.class,
        dto.getSortName(), dto.getSortType());
  }

  public BaseDto sqlSearch(MrCauseWoWasCompletedDTO dto) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CAUSE_WO_WAS_COMPLETED,
            "on-search-mr-cause-wo-was-completed");
    Map<String, Object> parameters = new HashMap<>();
    String leeLocale = I18n.getLocale();
    parameters.put("system", LANGUAGUE_EXCHANGE_SYSTEM.COMMON);
    parameters.put("business", APPLIED_BUSSINESS.CAT_ITEM.toString());
    parameters.put("leeLocale", leeLocale);
    parameters.put("categoryCode", MR_ITEM_NAME.BTS_REASON_WO_NOT_COMPLETE);
    if (dto != null) {
      if (!StringUtils.isStringNullOrEmpty(dto.getSearchAll())) {
        sqlQuery += " AND ( LOWER(T1.REASON_CODE) LIKE :searchAll ESCAPE '\\' OR LOWER(T1.REASON_NAME) LIKE :searchAll ESCAPE '\\' ) ";
        parameters.put("searchAll", StringUtils.convertLowerParamContains(dto.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getReasonCode())) {
        sqlQuery += " AND LOWER(T1.REASON_CODE) LIKE :reasonCode ESCAPE '\\' ";
        parameters.put("reasonCode", StringUtils.convertLowerParamContains(dto.getReasonCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getReasonName())) {
        sqlQuery += " AND LOWER(T1.REASON_NAME) LIKE :reasonName ESCAPE '\\' ";
        parameters.put("reasonName", StringUtils.convertLowerParamContains(dto.getReasonName()));
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getWaitingTime())) {
        sqlQuery += " AND T1.WAITING_TIME LIKE :waitingTime ESCAPE '\\' ";
        parameters.put("waitingTime",
            StringUtils.convertLowerParamContains(dto.getWaitingTime()));
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getValidateProcess())) {
        sqlQuery += " AND LOWER(T1.VALIDATE_PROCESS) LIKE :validateProcess ESCAPE '\\' ";
        parameters.put("validateProcess",
            StringUtils.convertLowerParamContains(dto.getValidateProcess()));
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getReasonType())) {
        sqlQuery += " AND T1.REASON_TYPE = :reasonType ";
        parameters.put("reasonType", dto.getReasonType());
      }
    }
    sqlQuery += " ORDER BY T1.ID DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public ResultInSideDto insertOrUpdate(MrCauseWoWasCompletedDTO mrCauseWoWasCompletedDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrCauseWoWasCompletedEntity entity = getEntityManager()
        .merge(mrCauseWoWasCompletedDTO.toEntity());
    resultInSideDto.setId(entity.getId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto checkExisted(String reasonCode, String reasonType, String id) {
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append(
        "select id from OPEN_PM.CAUSE_WO_WAS_COMPLETED WHERE 1=1 AND REASON_CODE = :reasonCode AND REASON_TYPE = :reasonType ");
    parameters.put("reasonCode", reasonCode);
    parameters.put("reasonType", reasonType);
    List<MrCauseWoWasCompletedDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(MrCauseWoWasCompletedDTO.class));
    if (!StringUtils.isStringNullOrEmpty(id)) {
      for (int i = 0; i < list.size(); i++) {
        if (list.get(i).getId().toString().equals(id)) {
          list.remove(i);
          break;
        }
      }
    }
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (list != null && list.size() > 0) {
      resultInSideDto.setCheck(true);
      resultInSideDto.setObject(list.get(0));
    } else {
      resultInSideDto.setCheck(false);
    }
    return resultInSideDto;
  }

  @Override
  public MrCauseWoWasCompletedDTO findById(Long id) {
    if (id != null) {
      return getEntityManager().find(MrCauseWoWasCompletedEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public List<MrCauseWoWasCompletedDTO> onSearchExport(MrCauseWoWasCompletedDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrCauseWoWasCompletedDTO.class));
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrCauseWoWasCompletedEntity entity = getEntityManager()
        .find(MrCauseWoWasCompletedEntity.class, id);
    if (entity != null) {
      getEntityManager().remove(entity);
    }
    return resultInSideDto;
  }

  @Override
  public List<MrCauseWoWasCompletedDTO> getReasonWO(String reasonTypeId) {
    if (StringUtils.isStringNullOrEmpty(reasonTypeId)) {
      return new ArrayList<>();
    }
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CAUSE_WO_WAS_COMPLETED, "get-reason-wo");
    Map<String, Object> params = new HashMap<>();
    params.put("reasonType", reasonTypeId);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(MrCauseWoWasCompletedDTO.class));
  }
}
