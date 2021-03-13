package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import com.viettel.gnoc.sr.model.SRRoleUserEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SRRoleUserRepositoryImpl extends BaseRepository implements SRRoleUserRepository {

  @Override
  public Datatable getListSRRoleUserPage(SRRoleUserInSideDTO srRoleUserDTO) {
    BaseDto baseDto = sqlSearch(srRoleUserDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        srRoleUserDTO.getPage(), srRoleUserDTO.getPageSize(),
        SRRoleUserInSideDTO.class,
        srRoleUserDTO.getSortName(), srRoleUserDTO.getSortType());
  }

  @Override
  public List<SRRoleUserInSideDTO> getListSRRoleUser(SRRoleUserInSideDTO srRoleUserDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_ROLE_USER, "getListSRRoleUser");
    Map<String, Object> parameters = new HashMap<>();
    if (srRoleUserDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getCountry())) {
        sql += " AND T1.COUNTRY= :country ";
        parameters.put("country", srRoleUserDTO.getCountry());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getUsername())) {
        sql += " AND lower(T1.USER_NAME)= :username ";
        parameters.put("username", srRoleUserDTO.getUsername());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getUnitId())) {
        sql += " AND T1.UNIT_ID= :unitId ";
        parameters.put("unitId", srRoleUserDTO.getUnitId());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getStatus())) {
        sql += " AND T1.STATUS= :status ";
        parameters.put("status", srRoleUserDTO.getStatus());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getGroupRole())) {
        String sqlR = " AND T2.GROUP_ROLE IN (";
        String[] arrStatus = srRoleUserDTO.getGroupRole().split(",");
        for (String s : arrStatus) {
          s = s.trim();
          sqlR += "'" + s + "'" + ",";
        }
        if (sqlR.endsWith(",")) {
          sqlR = sqlR.substring(0, sqlR.length() - 1);
        }
        sqlR += ") ";
        sql += (sqlR);
        sql += " ";
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getRoleCode())) {
        String sqlR = " AND T1.ROLE_CODE IN (";
        String[] arrStatus = srRoleUserDTO.getRoleCode().split(",");
        for (String s : arrStatus) {
          s = s.trim();
          sqlR += "'" + s + "'" + ",";
        }
        if (sqlR.endsWith(",")) {
          sqlR = sqlR.substring(0, sqlR.length() - 1);
        }
        sqlR += ") ";
        sql += (sqlR);
        sql += " ";
      }
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRRoleUserInSideDTO.class));
  }

  @Override
  public List<SRRoleUserInSideDTO> getListSRRoleUserByUnitId(SRRoleUserInSideDTO srRoleUserDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_ROLE_USER, "getListUserRoleByUnitId");
    Map<String, Object> parameters = new HashMap<>();
    if (srRoleUserDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getCountry())) {
        sql += " AND role.COUNTRY= :country ";
        parameters.put("country", srRoleUserDTO.getCountry());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getUnitId())) {
        sql += " AND role.UNIT_ID= :unitId ";
        parameters.put("unitId", srRoleUserDTO.getUnitId());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getRoleCode())) {
        String sqlR = " AND role.ROLE_CODE IN (";
        String[] arrStatus = srRoleUserDTO.getRoleCode().split(",");
        for (String s : arrStatus) {
          s = s.trim();
          sqlR += "'" + s + "'" + ",";
        }
        if (sqlR.endsWith(",")) {
          sqlR = sqlR.substring(0, sqlR.length() - 1);
        }
        sql += sqlR;
        sql += ") ";
      }
//      sql += " ORDER BY unitId ";
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRRoleUserInSideDTO.class));
  }

  @Override
  public ResultInSideDto add(SRRoleUserInSideDTO srRoleUserDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRRoleUserEntity srRoleUserEntity = getEntityManager().merge(srRoleUserDTO.toEntity());
    resultInSideDto.setId(srRoleUserEntity.getRoleUserId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto edit(SRRoleUserInSideDTO srRoleUserDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(srRoleUserDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long roleUserId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRRoleUserEntity srRoleUserEntity = getEntityManager()
        .find(SRRoleUserEntity.class, roleUserId);
    if (srRoleUserEntity != null) {
      getEntityManager().remove(srRoleUserEntity);
    }
    return resultInSideDto;
  }

  @Override
  public SRRoleUserInSideDTO getDetail(Long roleUserId) {
    SRRoleUserEntity srRoleUserEntity = getEntityManager().find(SRRoleUserEntity.class, roleUserId);
    SRRoleUserInSideDTO srRoleUserDTO = srRoleUserEntity.toDTO();
    return srRoleUserDTO;
  }


  @Override
  public ResultInSideDto insertOrUpdate(SRRoleUserInSideDTO srRoleUserDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    SRRoleUserEntity srRoleUserEntity = srRoleUserDTO.toEntity();
    if (srRoleUserEntity.getRoleUserId() != null) {
      getEntityManager().merge(srRoleUserEntity);
    } else {
      getEntityManager().persist(srRoleUserEntity);
    }
    resultInSideDTO.setId(srRoleUserEntity.getRoleUserId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto insertOrUpdateList(
      List<SRRoleUserInSideDTO> srRoleUserDTOList) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (SRRoleUserInSideDTO dto : srRoleUserDTOList) {
      resultInSideDto = insertOrUpdate(dto);
    }
    return resultInSideDto;
  }

  @Override
  public Datatable getListDataExport(SRRoleUserInSideDTO srRoleUserDTO) {
    BaseDto baseDto = sqlSearch(srRoleUserDTO);
    Datatable datatable = new Datatable();
    List<SRRoleUserInSideDTO> srRoleUserDTOList = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(SRRoleUserInSideDTO.class));

    datatable.setData(srRoleUserDTOList);
    return datatable;
  }

  @Override
  public SRRoleUserInSideDTO checkRoleUserExist(String country, String roleCode, String username,
      Long unitId) {

    List<SRRoleUserEntity> dataEntity = (List<SRRoleUserEntity>) findByMultilParam(
        SRRoleUserEntity.class,
        "country", country,
        "roleCode", roleCode,
        "username", username,
        "unitId", unitId);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public SRRoleUserInSideDTO checkRoleUserExistByUnitId(SRRoleUserInSideDTO srRoleUserDTO) {

    List<SRRoleUserEntity> dataEntity = (List<SRRoleUserEntity>) findByMultilParam(
        SRRoleUserEntity.class, "country", srRoleUserDTO.getCountry(), "unitId",
        srRoleUserDTO.getUnitId());
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public List<SRRoleUserInSideDTO> getListUser(String unitId, String country, String username) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR_ROLE_USER, "getListUser");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(unitId)) {
      sql += " AND T1.UNIT_ID = :unitId ";
      parameters.put("unitId", unitId);
    }
    if (!StringUtils.isStringNullOrEmpty(country)) {
      sql += " AND T1.COUNTRY = :country ";
      parameters.put("country", country);
    }
    if (!StringUtils.isStringNullOrEmpty(username)) {
      sql += " AND T1.USER_NAME = :username ";
      parameters.put("username", username);
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRRoleUserInSideDTO.class));
  }


  public BaseDto sqlSearch(SRRoleUserInSideDTO srRoleUserDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_ROLE_USER, "SR-Role-User-List");
    Map<String, Object> parameters = new HashMap<>();
    if (srRoleUserDTO != null) {
      if (StringUtils.isNotNullOrEmpty(srRoleUserDTO.getSearchAll())) {
        sqlQuery += " AND (lower(T1.USER_NAME) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(srRoleUserDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getCountry())) {
        sqlQuery += " AND T1.COUNTRY= :country ";
        parameters.put("country", srRoleUserDTO.getCountry());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getUsername())) {
        sqlQuery += " AND lower(T1.USER_NAME)= :username ";
        parameters.put("username", srRoleUserDTO.getUsername());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getUnitId())) {
        sqlQuery += " AND T1.UNIT_ID= :unitId ";
        parameters.put("unitId", srRoleUserDTO.getUnitId());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getStatus())) {
        sqlQuery += " AND T1.STATUS= :status ";
        parameters.put("status", srRoleUserDTO.getStatus());
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getRoleCode())) {
        sqlQuery += " AND (lower(T1.ROLE_CODE) LIKE :roleCode ESCAPE '\\' )";
        parameters
            .put("roleCode", StringUtils.convertLowerParamContains(srRoleUserDTO.getRoleCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getIsLeader())) {
        sqlQuery += " AND T1.IS_LEADER= :isLeader ";
        parameters.put("isLeader", srRoleUserDTO.getIsLeader());
      }
    }
    sqlQuery += " ORDER BY T1.ROLE_USER_ID DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
