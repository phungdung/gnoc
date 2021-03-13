package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrRolesDTO;
import com.viettel.gnoc.cr.model.CrRolesEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DungPV
 */
@Repository
@Transactional
@Slf4j
public class CrRolesRepositoryImpl extends BaseRepository implements CrRolesRepository {

  @Override
  public BaseDto sqlSearch(CrRolesDTO crRolesDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_Cr_Manager_Roles, "CrManagerRoles-list");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    if (StringUtils.isNotNullOrEmpty(crRolesDTO.getSearchAll())) {
      StringBuilder sqlSearchAll = new StringBuilder(
          " AND (lower(r.CMRE_CODE) LIKE :searchAll ESCAPE '\\' ");
      sqlSearchAll.append(" OR lower(r.CMRE_NAME) LIKE :searchAll ESCAPE '\\' ) ");
      sqlQuery += sqlSearchAll.toString();
      parameters.put("searchAll", StringUtils.convertLowerParamContains(crRolesDTO.getSearchAll()));
    }
    if (StringUtils.isNotNullOrEmpty(crRolesDTO.getCmreCode())) {
      sqlQuery += " AND LOWER(r.CMRE_CODE) LIKE :cmreCode ESCAPE '\\' ";
      parameters.put("cmreCode", StringUtils.convertLowerParamContains(crRolesDTO.getCmreCode()));
    }
    if (StringUtils.isNotNullOrEmpty(crRolesDTO.getCmreName())) {
      sqlQuery += " AND LOWER(r.CMRE_NAME) LIKE :cmreName ESCAPE '\\' ";
      parameters.put("cmreName", StringUtils.convertLowerParamContains(crRolesDTO.getCmreName()));
    }
//    sqlQuery += " order by r.CMRE_CODE ASC ";
    sqlQuery += " order by r.CMRE_ID DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public Datatable getListCrRoles(CrRolesDTO crRolesDTO) {
    BaseDto baseDto = sqlSearch(crRolesDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        crRolesDTO.getPage(), crRolesDTO.getPageSize(), CrRolesDTO.class,
        crRolesDTO.getSortName(), crRolesDTO.getSortType());
    return datatable;
  }

  @Override
  public CrRolesDTO getDetail(Long cmreId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_Cr_Manager_Roles, "CrManagerRoles-list");
    sqlQuery += " AND r.CMRE_ID =:cmreId ";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    parameters.put("cmreId", cmreId);
    List<CrRolesDTO> lst = getNamedParameterJdbcTemplate().query(
        sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CrRolesDTO.class));
    if (lst.isEmpty()) {
      return null;
    }
    return lst.get(0);
  }

  @Override
  public ResultInSideDto deleteCrRoles(Long id) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(Constants.RESULT.SUCCESS);
    CrRolesEntity entity = getEntityManager().find(CrRolesEntity.class, id);
    if (entity != null) {
      getEntityManager().remove(entity);
    }
    return resultDto;
  }


  @Override
  public ResultInSideDto addOrEdit(CrRolesDTO crRolesDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(Constants.RESULT.SUCCESS);
    CrRolesEntity crRolesEntity = getEntityManager().merge(crRolesDTO.toEntity());
    resultDto.setId(crRolesEntity.getCmreId());
    return resultDto;
  }
}
