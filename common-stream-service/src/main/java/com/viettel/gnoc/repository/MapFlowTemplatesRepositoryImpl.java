package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MapFlowTemplatesDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.MapFlowTemplatesEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MapFlowTemplatesRepositoryImpl extends BaseRepository implements
    MapFlowTemplatesRepository {

  @Override
  public ResultInSideDto updateMapFlowTemplates(MapFlowTemplatesDTO mapFlowTemplatesDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MapFlowTemplatesEntity entity = mapFlowTemplatesDTO.toEntity();
    getEntityManager().merge(entity);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteMapFlowTemplates(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(deleteById(MapFlowTemplatesEntity.class, id, colId));
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListMapFlowTemplates(
      List<MapFlowTemplatesDTO> mapFlowTemplatesListDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    String result = deleteByListDTO(mapFlowTemplatesListDTO, MapFlowTemplatesEntity.class, colId);
    resultInSideDto.setKey(result);
    return resultInSideDto;
  }

  @Override
  public MapFlowTemplatesDTO findMapFlowTemplatesById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(MapFlowTemplatesEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertMapFlowTemplates(MapFlowTemplatesDTO mapFlowTemplatesDTO) {
    return insertByModel(mapFlowTemplatesDTO.toEntity(), colId);
  }

  @Override
  public ResultInSideDto insertOrUpdateListMapFlowTemplates(
      List<MapFlowTemplatesDTO> mapFlowTemplatesDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    for (MapFlowTemplatesDTO item : mapFlowTemplatesDTO) {
      MapFlowTemplatesEntity entity = item.toEntity();
      if (entity.getId() != null && entity.getId() > 0) {
        getEntityManager().merge(entity);
      } else {
        getEntityManager().persist(entity);
      }
    }
    return resultInSideDto;
  }

  @Override
  public List<String> getSequenseMapFlowTemplates(String seqName, int... size) {
    return getListSequense(seqName, size);
  }

  @Override
  public List<MapFlowTemplatesDTO> getListMapFlowTemplatesByCondition(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    return null;
  }

  @Override
  public Datatable getListMapFlowTemplatesDTO(MapFlowTemplatesDTO mapFlowTemplatesDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById("mapFlowTemplates", "getListMapFlowTemplatesDTO");
    String locale = I18n.getLocale();
    parameters.put("p_leeLocale", locale);
    if (!StringUtils.isStringNullOrEmpty(mapFlowTemplatesDTO.getTypeId())) {
      sql += "  and a.typeid= :typeid ";
      parameters.put("typeid", mapFlowTemplatesDTO.getTypeId());
    }
    if (!StringUtils.isStringNullOrEmpty(mapFlowTemplatesDTO.getAlarmGroupId())) {
      sql += " and a.alarmGroupId= :alarmGroupId ";
      parameters.put("alarmGroupId", mapFlowTemplatesDTO.getAlarmGroupId());
    }
    if (!StringUtils.isStringNullOrEmpty(mapFlowTemplatesDTO.getUserID())) {
      sql += " and a.userID= :userID ";
      parameters.put("userID", mapFlowTemplatesDTO.getUserID());
    }
    if (!StringUtils.isStringNullOrEmpty(mapFlowTemplatesDTO.getSearchEndTime()) && !StringUtils
        .isStringNullOrEmpty(mapFlowTemplatesDTO.getSearchStartTime())) {
      sql += " and  ( a.lastUpdateTime BETWEEN to_date(:searchStartTime, 'dd/MM/yyyy HH24:MI:ss') and to_date(:searchEndTime, 'dd/MM/yyyy HH24:MI:ss') ) ";
      parameters.put("searchStartTime", mapFlowTemplatesDTO.getSearchStartTime());
      parameters.put("searchEndTime", mapFlowTemplatesDTO.getSearchEndTime());
    }
    if (!StringUtils.isStringNullOrEmpty(mapFlowTemplatesDTO.getUpdateUser())) {
      sql += " and  a.userID IN(select USER_ID from COMMON_GNOC.USERS  where LOWER(FULLNAME) LIKE :updateUser ESCAPE '\\')";
      parameters.put("updateUser",
          StringUtils.convertLowerParamContains(mapFlowTemplatesDTO.getUpdateUser()));
    }
    if (StringUtils.isNotNullOrEmpty(mapFlowTemplatesDTO.getSearchAll())) {
      sql += " AND (LOWER( a.typeName) LIKE LOWER(:searchAll) ESCAPE '\\'";
      sql += " OR LOWER(a.alarmGroupName) LIKE LOWER(:searchAll) ESCAPE '\\'";
      sql += " OR LOWER(a.lastUpdateTime) LIKE LOWER(:searchAll) ESCAPE '\\'";
      sql += " OR  a.userID IN(select USER_ID from COMMON_GNOC.USERS  where LOWER(FULLNAME) LIKE :searchAll ESCAPE '\\')) order by a.typeName ";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(mapFlowTemplatesDTO.getSearchAll()));
    }

    return getListDataTableBySqlQuery(sql, parameters, mapFlowTemplatesDTO.getPage(),
        mapFlowTemplatesDTO.getPageSize(),
        MapFlowTemplatesDTO.class, mapFlowTemplatesDTO.getSortName(),
        mapFlowTemplatesDTO.getSortType());
  }

  @Override
  public List<MapFlowTemplatesDTO> getListMapFlowTemplates(MapFlowTemplatesDTO mapFlowTemplatesDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = "select * from MAP_FLOW_TEMPLATES a where 1=1";
    if (!StringUtils.isStringNullOrEmpty(mapFlowTemplatesDTO.getTypeId())) {
      sql += " and a.TYPE_ID= :typeId";
      parameters.put("typeId", mapFlowTemplatesDTO.getTypeId());
    }
    if (!StringUtils.isStringNullOrEmpty(mapFlowTemplatesDTO.getAlarmGroupId())) {
      sql += " and a.ALARM_GROUP_ID= :alarmGroupId";
      parameters.put("alarmGroupId", mapFlowTemplatesDTO.getAlarmGroupId());
    }
    List<MapFlowTemplatesDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MapFlowTemplatesDTO.class));
    return list;
  }

  private static final String colId = "id";
}

