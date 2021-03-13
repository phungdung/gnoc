package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CoordinationSettingDTO;
import com.viettel.gnoc.cr.model.CoordinationSettingEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CoordinationSettinggRepositoryImpl extends BaseRepository implements
    CoordinationSettinggRepository {

  @Override
  public Datatable getListCoordinationSettingPage(CoordinationSettingDTO coordinationSettingDTO) {
    BaseDto baseDto = sqlSearch(coordinationSettingDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        coordinationSettingDTO.getPage(), coordinationSettingDTO.getPageSize(),
        CoordinationSettingDTO.class,
        coordinationSettingDTO.getSortName(), coordinationSettingDTO.getSortType());
  }

  @Override
  public ResultInSideDto insertOrUpdate(CoordinationSettingDTO coordinationSettingDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CoordinationSettingEntity coordinationSettingEntity = getEntityManager()
        .merge(coordinationSettingDTO.toEntity());
    resultInSideDto.setId(coordinationSettingEntity.getId());
    return resultInSideDto;
  }

  @Override
  public CoordinationSettingDTO getDetail(Long id) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_COORDINATION_SETTING,
            "get_detail_coordinationSetting_byId");
    if (!StringUtils.isStringNullOrEmpty(id)) {
      sqlQuery += " AND a.ID = :id ";
      parameters.put("id", id);
    }
    List<CoordinationSettingDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters,
            BeanPropertyRowMapper.newInstance(CoordinationSettingDTO.class));
    return list.get(0);
  }

  @Override
  public ResultInSideDto deleteCoordinationSetting(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CoordinationSettingEntity coordinationSettingEntity = getEntityManager()
        .find(CoordinationSettingEntity.class, id);
    getEntityManager().remove(coordinationSettingEntity);
    return resultInSideDto;
  }

  public BaseDto sqlSearch(CoordinationSettingDTO coordinationSettingDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_COORDINATION_SETTING,
            "coordination-Setting-list");
    Map<String, Object> parameters = new HashMap<>();
    if (coordinationSettingDTO != null) {
      if (StringUtils.isNotNullOrEmpty(coordinationSettingDTO.getSearchAll())) {
        sqlQuery += " AND (LOWER(c.UNIT_CODE) LIKE :searchAll ESCAPE '\\' OR LOWER(c.UNIT_NAME) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(coordinationSettingDTO.getSearchAll()));
      }

      if (!StringUtils.isStringNullOrEmpty(coordinationSettingDTO.getUnitCode())) {
        sqlQuery += " and LOWER(c.UNIT_CODE) like :unitCode escape '\\' ";
        parameters
            .put("unitCode",
                StringUtils.convertLowerParamContains(coordinationSettingDTO.getUnitCode()));
      }

      if (!StringUtils.isStringNullOrEmpty(coordinationSettingDTO.getUnitName())) {
        sqlQuery += " and LOWER(c.UNIT_NAME) like :unitName escape '\\' ";
        parameters
            .put("unitName",
                StringUtils.convertLowerParamContains(coordinationSettingDTO.getUnitName()));
      }

      if (!StringUtils.isStringNullOrEmpty(coordinationSettingDTO.getGroupCode())) {
        sqlQuery += " and LOWER(b.WO_GROUP_CODE) like :groupCode escape '\\' ";
        parameters
            .put("groupCode",
                StringUtils.convertLowerParamContains(coordinationSettingDTO.getGroupCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(coordinationSettingDTO.getGroupName())) {
        sqlQuery += " and LOWER(b.WO_GROUP_NAME) like :groupName escape '\\' ";
        parameters
            .put("groupName",
                StringUtils.convertLowerParamContains(coordinationSettingDTO.getGroupName()));
      }

    }
    sqlQuery += "ORDER BY a.id DESC";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
