package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.TT_MASTER_CODE;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.TTChangeStatusDTO;
import com.viettel.gnoc.incident.model.TTChangeStatusEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TTChangeStatusRepositoryImpl extends BaseRepository implements
    TTChangeStatusRepository {

  public BaseDto sqlSearch(TTChangeStatusDTO ttChangeStatusDTO) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TT_CHANGE_STATUS_BUSINESS,
        "get-data-TT-Change-Status-Search-Web");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("categoryCode", TT_MASTER_CODE.TT_STATUS);
    parameters.put("p_leeLocale", I18n.getLocale());
    parameters.put("p_system", "COMMON_GNOC");
    parameters.put("p_bussiness", "COMMON_GNOC.CAT_ITEM");
    if (!StringUtils.isLongNullOrEmpty(ttChangeStatusDTO.getTtTypeId())) {
      sql += " and tt.TT_TYPE_ID =:ttTypeId ";
      parameters.put("ttTypeId", ttChangeStatusDTO.getTtTypeId());
    }
    if (StringUtils.isNotNullOrEmpty(ttChangeStatusDTO.getAlarmGroup())) {
      sql += " and tt.ALARM_GROUP =:alarmGroup ";
      parameters.put("alarmGroup", ttChangeStatusDTO.getAlarmGroup());
    }
    if (!StringUtils.isLongNullOrEmpty(ttChangeStatusDTO.getOldStatus())) {
      sql += " and tt.OLD_STATUS =:oldStatus ";
      parameters.put("oldStatus", ttChangeStatusDTO.getOldStatus());
    }
    if (!StringUtils.isLongNullOrEmpty(ttChangeStatusDTO.getNewStatus())) {
      sql += " and tt.NEW_STATUS =:newStatus ";
      parameters.put("newStatus", ttChangeStatusDTO.getNewStatus());
    }
    if (!StringUtils.isLongNullOrEmpty(ttChangeStatusDTO.getIsDefault())) {
      sql += " and NVL(tt.IS_DEFAULT,0) =:isDefault ";
      parameters.put("isDefault", ttChangeStatusDTO.getIsDefault());
    }
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public Datatable getListTTChangeStatus(TTChangeStatusDTO ttChangeStatusDTO) {
    BaseDto baseDto = sqlSearch(ttChangeStatusDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        ttChangeStatusDTO.getPage(), ttChangeStatusDTO.getPageSize(),
        TTChangeStatusDTO.class, ttChangeStatusDTO.getSortName(),
        ttChangeStatusDTO.getSortType());
  }

  @Override
  public TTChangeStatusDTO findTTChangeStatusDTO(TTChangeStatusDTO ttChangeStatusDTO) {

    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_TT_CHANGE_STATUS_BUSINESS,
            "get-tt-change-status-dto-by-typeId-alarmGroup");
    Map<String, Object> params = new HashMap<>();
    params.put("p_leeLocale", I18n.getLocale());
    params.put("p_system", "COMMON_GNOC");
    params.put("p_bussiness", "COMMON_GNOC.CAT_ITEM");
    params.put("categoryCode", TT_MASTER_CODE.TT_STATUS);
    if (!StringUtils.isLongNullOrEmpty(ttChangeStatusDTO.getTtTypeId())) {
      sqlQuery += " and ttcs.TT_TYPE_ID = :typeId ";
      params.put("typeId", ttChangeStatusDTO.getTtTypeId());
    }
    if (StringUtils.isNotNullOrEmpty(ttChangeStatusDTO.getAlarmGroup())) {
      sqlQuery += " and ttcs.ALARM_GROUP = :alarmGroup ";
      params.put("alarmGroup", ttChangeStatusDTO.getAlarmGroup());
    }
    if (!StringUtils.isLongNullOrEmpty(ttChangeStatusDTO.getOldStatus())) {
      sqlQuery += " and ttcs.OLD_STATUS = :oldStatus ";
      params.put("oldStatus", ttChangeStatusDTO.getOldStatus());
    }
    if (!StringUtils.isLongNullOrEmpty(ttChangeStatusDTO.getNewStatus())) {
      sqlQuery += " and ttcs.NEW_STATUS = :newStatus ";
      params.put("newStatus", ttChangeStatusDTO.getNewStatus());
    }
    if (!StringUtils.isLongNullOrEmpty(ttChangeStatusDTO.getIsDefault())
        && ttChangeStatusDTO.getIsDefault() == 1L) {
      sqlQuery += " and ttcs.IS_DEFAULT = 1 ";
    }
    List<TTChangeStatusDTO> odChangeStatusDTOS = getNamedParameterJdbcTemplate()
        .query(sqlQuery, params, BeanPropertyRowMapper.newInstance(TTChangeStatusDTO.class));

    if (odChangeStatusDTOS != null && odChangeStatusDTOS.size() > 0) {
      return odChangeStatusDTOS.get(0);
    }
    return null;
  }

  @Override
  public TTChangeStatusDTO findTTChangeStatusById(Long id) {
    TTChangeStatusEntity entity = getEntityManager().find(TTChangeStatusEntity.class, id);
    if (entity != null) {
      return entity.toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertOrUpdateTTChangeStatus(TTChangeStatusDTO ttChangeStatusDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    TTChangeStatusEntity entity = getEntityManager().merge(ttChangeStatusDTO.toEntity());
    resultInSideDTO.setId(entity.getId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteTTChangeStatus(Long ttChangeStatusId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    TTChangeStatusEntity entity = getEntityManager()
        .find(TTChangeStatusEntity.class, ttChangeStatusId);
    getEntityManager().remove(entity);
    return resultInSideDto;
  }

}
