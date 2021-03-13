package com.viettel.gnoc.cr.repository;


import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrRoleDeptDTO;
import com.viettel.gnoc.cr.dto.CrRoleDeptDTOSearch;
import com.viettel.gnoc.cr.dto.CrRolesDTO;
import com.viettel.gnoc.cr.model.CrRoleDeptEntity;
import com.viettel.gnoc.cr.model.CrRolesEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CrRolesDeptRepositoryImpl extends BaseRepository implements CrRolesDeptRepository {

  @Override
  public List<CrRolesDTO> getListCrRolesDTO(CrRolesDTO crRolesDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = " select a.CMRE_ID cmreId,a.CMRE_CODE cmreCode,a.CMRE_NAME cmreName,a.DESCRIPTION description,a.IS_ACTIVE status,a.IS_SCHEDULE_CR_EMERGENCY isScheduleCrEmergency from OPEN_PM.CR_MANAGER_ROLE a where IS_ACTIVE=1  ORDER BY CMRE_NAME";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper
            .newInstance(CrRolesDTO.class));
  }

  @Override
  public ResultInSideDto insertCrRoles(CrRolesDTO crRolesDTO) {
    return insertByModel(crRolesDTO.toEntity(), colId);
  }

  @Override
  public ResultInSideDto updateCrRoles(CrRolesDTO crRolesDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(crRolesDTO.toEntity());
    return resultDto;

  }

  @Override
  public ResultInSideDto deleteRoles(Long id) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(deleteById(CrRolesEntity.class, id, colId));
    return resultDto;
  }

  @Override
  public ResultInSideDto insertRoleDept(CrRoleDeptDTO crRoleDeptDTO) {
    return insertByModel(crRoleDeptDTO.toEntity(), "cmroutId");
  }

  @Override
  public ResultInSideDto updateRoleDept(CrRoleDeptDTO crRoleDeptDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(RESULT.SUCCESS);
    resultDto.setId(crRoleDeptDTO.getCmroutId());
    getEntityManager().merge(crRoleDeptDTO.toEntity());
    return resultDto;
  }

  @Override
  public ResultInSideDto deleteRoleDept(Long id) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(deleteById(CrRoleDeptEntity.class, id, "cmroutId"));
    return resultDto;
  }

  @Override
  public CrRoleDeptEntity findCrRoleDeptEntityById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(CrRoleDeptEntity.class, id);
    }
    return null;
  }

  @Override
  public CrRolesDTO findCrRolesDTOById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(CrRolesEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public Datatable getListRoleDept(CrRoleDeptDTOSearch crRoleDeptDTOSearch) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById("crRoleDept", "getListRoleDept");
    String locale = I18n.getLocale();
    parameters.put("leeLocale", locale);
    if (!StringUtils.isStringNullOrEmpty(crRoleDeptDTOSearch.getUnitId())) {
      sql += " and re.unitId = :unitId";
      parameters.put("unitId", crRoleDeptDTOSearch.getUnitId());
    }
    if (!StringUtils.isStringNullOrEmpty(crRoleDeptDTOSearch.getCmreId())) {
      sql += " and re.cmreid = :cmreid";
      parameters.put("cmreid", crRoleDeptDTOSearch.getCmreId());
    }
    if (!StringUtils.isStringNullOrEmpty(crRoleDeptDTOSearch.getParentUnitId())) {
      sql += " and re.parentUnitId = :parentUnitId";
      parameters.put("parentUnitId", crRoleDeptDTOSearch.getParentUnitId());
    }
    if (!StringUtils.isStringNullOrEmpty(crRoleDeptDTOSearch.getSearchAll())) {
      sql += " and (LOWER(re.cmreName) LIKE LOWER(:searchAll) ESCAPE '\\'";
      sql += " OR LOWER(re.cmreCode) LIKE LOWER(:searchAll) ESCAPE '\\' ) ";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(crRoleDeptDTOSearch.getSearchAll()));
    }
    if (!StringUtils.isStringNullOrEmpty(crRoleDeptDTOSearch.getCmreCode())) {
      sql += " and re.cmreCode =:cmreCode ";
      parameters.put("cmreCode", crRoleDeptDTOSearch.getCmreCode());
    }
    sql += " order by re.cmroutId DESC ";

    return getListDataTableBySqlQuery(sql, parameters, crRoleDeptDTOSearch.getPage(),
        crRoleDeptDTOSearch.getPageSize(),
        CrRoleDeptDTOSearch.class, crRoleDeptDTOSearch.getSortName(),
        crRoleDeptDTOSearch.getSortType());
  }

  private final static String colId = "cmreId";
}
