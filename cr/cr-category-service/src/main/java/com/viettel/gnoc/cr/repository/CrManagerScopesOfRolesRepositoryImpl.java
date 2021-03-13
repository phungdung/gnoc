package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrManagerRolesDTO;
import com.viettel.gnoc.cr.dto.CrManagerScopeDTO;
import com.viettel.gnoc.cr.dto.CrManagerScopesOfRolesDTO;
import com.viettel.gnoc.cr.model.CrManagerScopesOfRolesEntity;
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
@Slf4j
@Transactional
public class CrManagerScopesOfRolesRepositoryImpl extends BaseRepository implements
    CrManagerScopesOfRolesRepository {

  @Override
  public BaseDto sqlSearch(CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = "";
    sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_Cr_Manager_Scopes_Of_Roles,
            "Cr-ManagerScopesOfRoles-list");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    if (StringUtils.isNotNullOrEmpty(crManagerScopesOfRolesDTO.getSearchAll())) {
      StringBuilder sqlSearchAll = new StringBuilder(
          " AND (lower(ls.cmse_code) LIKE :searchAll ESCAPE '\\' ");
      sqlSearchAll.append(" OR lower(ls.cmse_name) LIKE :searchAll ESCAPE '\\' ) ");
      sqlQuery += sqlSearchAll.toString();
      parameters
          .put("searchAll",
              StringUtils.convertLowerParamContains(crManagerScopesOfRolesDTO.getSearchAll()));
    }
    if (StringUtils.isNotNullOrEmpty(crManagerScopesOfRolesDTO.getCmseCode())) {
      sqlQuery += " AND LOWER(ls.cmse_code) LIKE :CMSE_CODE ESCAPE '\\'";
      parameters
          .put("CMSE_CODE", StringUtils.convertLowerParamContains
              (crManagerScopesOfRolesDTO.getCmseCode()));
    }
    if (StringUtils.isNotNullOrEmpty(crManagerScopesOfRolesDTO.getCmseName())) {
      sqlQuery += " AND LOWER(ls.cmse_name) LIKE :CMSE_NAME ESCAPE '\\' ";
      parameters
          .put("CMSE_NAME", StringUtils.convertLowerParamContains
              (crManagerScopesOfRolesDTO.getCmseName()));
    }
    if (StringUtils.isNotNullOrEmpty(crManagerScopesOfRolesDTO.getCmreCode())) {
      sqlQuery += " AND LOWER(ls.cmre_code) LIKE :CMRE_CODE ESCAPE '\\' ";
      parameters.put("CMRE_CODE",
          StringUtils.convertLowerParamContains
              (crManagerScopesOfRolesDTO.getCmreCode()));
    }
    if (StringUtils.isNotNullOrEmpty(crManagerScopesOfRolesDTO.getCmreName())) {
      sqlQuery += " AND LOWER(ls.cmre_name) LIKE :CMRE_NAME ESCAPE '\\' ";
      parameters.put("CMRE_NAME",
          StringUtils.convertLowerParamContains
              (crManagerScopesOfRolesDTO.getCmreName()));
    }
    if (crManagerScopesOfRolesDTO.getCmsorsId() != null
        && crManagerScopesOfRolesDTO.getCmsorsId() > 0) {
      sqlQuery += " AND ls.CMSORS_ID =:CMSORS_ID ";
      parameters.put("CMSORS_ID", crManagerScopesOfRolesDTO.getCmsorsId());
    }
//    sqlQuery += " ORDER BY ls.cmre_code ASC ";
    sqlQuery += " ORDER BY ls.CMSORS_ID DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public Datatable getListManagerScopesOfRoles(
      CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO) {
    BaseDto baseDto = sqlSearch(crManagerScopesOfRolesDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(), crManagerScopesOfRolesDTO.getPage()
        , crManagerScopesOfRolesDTO.getPageSize()
        , CrManagerScopesOfRolesDTO.class, crManagerScopesOfRolesDTO.getSortName()
        , crManagerScopesOfRolesDTO.getSortType());
  }

  @Override
  public ResultInSideDto addOrEdit(CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    CrManagerScopesOfRolesEntity addOrUpdate = getEntityManager()
        .merge(crManagerScopesOfRolesDTO.toModel());
    resultInSideDto.setId(addOrUpdate.getCmsorsId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteManagerScopesOfRoles(Long cmsorsId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    CrManagerScopesOfRolesEntity entity = getEntityManager()
        .find(CrManagerScopesOfRolesEntity.class, cmsorsId);
    if (entity != null) {
      getEntityManager().remove(entity);
    }
    return resultInSideDTO;
  }

  @Override
  public List<CrManagerScopesOfRolesDTO> getListDataExport(
      CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO) {
    BaseDto baseDto = sqlSearch(crManagerScopesOfRolesDTO);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(CrManagerScopesOfRolesDTO.class));
  }

  @Override
  public CrManagerScopesOfRolesDTO getDetail(Long crId) {
    CrManagerScopesOfRolesDTO crManagerScopesOfRolesDTO = new CrManagerScopesOfRolesDTO();
    crManagerScopesOfRolesDTO.setCmsorsId(crId);
    BaseDto baseDto = sqlSearch(crManagerScopesOfRolesDTO);
    List<CrManagerScopesOfRolesDTO> lst = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(), BeanPropertyRowMapper.newInstance(
            CrManagerScopesOfRolesDTO.class));
    return lst.isEmpty() ? null : lst.get(0);
  }

  @Override
  public List<CrManagerRolesDTO> getListCrManagerRolesCBB() {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_Cr_Manager_Scopes_Of_Roles,
            "getListCrManagerRolesCBB");
    List<CrManagerRolesDTO> lst = getNamedParameterJdbcTemplate()
        .query(sqlQuery, BeanPropertyRowMapper.newInstance(
            CrManagerRolesDTO.class));
    return lst;
  }

  @Override
  public List<CrManagerScopeDTO> getListManagerScopeCBB() {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_Cr_Manager_Scopes_Of_Roles,
            "getListManagerScopeCBB");
    List<CrManagerScopeDTO> lst = getNamedParameterJdbcTemplate()
        .query(sqlQuery, BeanPropertyRowMapper.newInstance(
            CrManagerScopeDTO.class));
    return lst;
  }

}
