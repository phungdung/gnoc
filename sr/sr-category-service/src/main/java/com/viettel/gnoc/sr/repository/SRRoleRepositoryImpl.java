package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.model.SRRoleEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SRRoleRepositoryImpl extends BaseRepository implements SRRoleRepository {

  @Override
  public Datatable getListSRRolePage(SRRoleDTO srRoleDTO) {
    BaseDto baseDto = sqlSearch(srRoleDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        srRoleDTO.getPage(), srRoleDTO.getPageSize(),
        SRRoleDTO.class,
        srRoleDTO.getSortName(), srRoleDTO.getSortType());
  }

  @Override
  public ResultInSideDto add(SRRoleDTO srRoleDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRRoleEntity srRoleEntity = getEntityManager().merge(srRoleDTO.toEntity());
    resultInSideDto.setId(srRoleEntity.getRoleId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto edit(SRRoleDTO srRoleDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(srRoleDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long roleId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRRoleEntity srRoleEntity = getEntityManager()
        .find(SRRoleEntity.class, roleId);
    if (srRoleEntity != null) {
      getEntityManager().remove(srRoleEntity);
    }
    return resultInSideDto;
  }

  @Override
  public SRRoleDTO getDetail(Long roleId) {
    SRRoleEntity srRoleEntity = getEntityManager().find(SRRoleEntity.class, roleId);
    SRRoleDTO srRoleDTO = srRoleEntity.toDTO();
    return srRoleDTO;
  }

  @Override
  public List<SRRoleDTO> getListSRRoleByLocationCBB(SRRoleDTO srRoleDTO) {
    String sqlQuery = "Select * From OPEN_PM.SR_ROLE T2 where 1=1";
    Map<String, Object> parameters = new HashMap<>();
    if (srRoleDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(srRoleDTO.getCountry())) {
        sqlQuery += " AND T2.COUNTRY= :country ";
        parameters.put("country", srRoleDTO.getCountry());
      }
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRRoleDTO.class));
  }

  public BaseDto sqlSearch(SRRoleDTO srRoleDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_ROLE, "sr-role-list");
    Map<String, Object> parameters = new HashMap<>();
    if (srRoleDTO != null) {
      if (StringUtils.isNotNullOrEmpty(srRoleDTO.getSearchAll())) {
        sqlQuery += " AND (lower(T1.ROLE_CODE) LIKE :searchAll ESCAPE '\\' OR lower(T1.ROLE_NAME) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(srRoleDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleDTO.getCountry())) {
        sqlQuery += " AND T1.COUNTRY= :country ";
        parameters.put("country", srRoleDTO.getCountry());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleDTO.getParentCode())) {
        sqlQuery += " AND T1.PARENT_CODE= :parentCode ";
        parameters.put("parentCode", srRoleDTO.getParentCode());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleDTO.getGroupRole())) {
        sqlQuery += " AND T1.GROUP_ROLE= :groupRole ";
        parameters.put("groupRole", srRoleDTO.getGroupRole());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleDTO.getRoleCode())) {
        sqlQuery += " AND (lower(T1.ROLE_CODE) LIKE :roleCode ESCAPE '\\' )";
        parameters.put("roleCode", StringUtils.convertLowerParamContains(srRoleDTO.getRoleCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleDTO.getRoleName())) {
        sqlQuery += " AND (lower(T1.ROLE_NAME) LIKE :roleName ESCAPE '\\' )";
        parameters.put("roleName", StringUtils.convertLowerParamContains(srRoleDTO.getRoleName()));
      }
      parameters.put("p_leeLocale", I18n.getLocale());
    }
    sqlQuery += " ORDER BY T1.ROLE_ID DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public Datatable getListDataExport(SRRoleDTO srRoleDTO) {
    BaseDto baseDto = sqlSearch(srRoleDTO);
    Datatable datatable = new Datatable();
    List<SRRoleDTO> srRoleDTOList = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(SRRoleDTO.class));

    datatable.setData(srRoleDTOList);
    return datatable;
  }

  @Override
  public SRRoleDTO checkRoleExist(String country, String roleCode) {

    List<SRRoleEntity> dataEntity = (List<SRRoleEntity>) findByMultilParam(
        SRRoleEntity.class,
        "country", country,
        "roleCode", roleCode);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertOrUpdateList(
      List<SRRoleDTO> srRoleDTOList) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (SRRoleDTO dto : srRoleDTOList) {
      resultInSideDto = add(dto);
    }
    return resultInSideDto;
  }

  @Override
  public List<SRRoleDTO> getListSRRoleDTO(SRRoleDTO srRoleDTO) {
    BaseDto baseDto = sqlSearch(srRoleDTO);
    List<SRRoleDTO> lstSrRole = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(SRRoleDTO.class));
    return lstSrRole;
  }
}
