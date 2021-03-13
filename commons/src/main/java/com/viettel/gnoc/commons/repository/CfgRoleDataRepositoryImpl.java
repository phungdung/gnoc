package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CfgRoleDataDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.CfgRoleDataEntity;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgRoleDataRepositoryImpl extends BaseRepository implements CfgRoleDataRepository {

  private static final String colId = "id";

  @Override
  public CfgRoleDataDTO getConfigByDto(CfgRoleDataDTO cfgRoleDataDTO) {
    BaseDto baseDto = sqlSearch(cfgRoleDataDTO);
    List<CfgRoleDataDTO> lst = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(CfgRoleDataDTO.class));
    if (lst != null && !lst.isEmpty()) {
      return lst.get(0);
    }
    return null;
  }

  @Override
  public Datatable onSearchCfgRoleData(CfgRoleDataDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        dto.getPage(), dto.getPageSize(),
        CfgRoleDataDTO.class,
        dto.getSortName(), dto.getSortType());
  }

  @Override
  public ResultInSideDto insertCfgRoleData(CfgRoleDataDTO dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgRoleDataEntity cfgRoleDataEntity = getEntityManager().merge(dto.toEntity());
    resultInSideDto.setId(cfgRoleDataEntity.getId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateCfgRoleData(CfgRoleDataDTO dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (checkExisted(dto.getUsername(), dto.getSystem().toString(), dto.getId().toString()).getCheck()) {
      resultInSideDto.setKey(RESULT.DUPLICATE);
      resultInSideDto.setMessage(I18n.getValidation("cfgRoleData.null.unique"));
      return resultInSideDto;
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgRoleDataEntity cfgRoleDataEntity = getEntityManager().merge(dto.toEntity());
    resultInSideDto.setId(cfgRoleDataEntity.getId());
    return resultInSideDto;
  }

  @Override
  public CfgRoleDataDTO findCfgRoleDataById(Long id) {
    if (id != null) {
      return getEntityManager().find(CfgRoleDataEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public String deleteCfgRoleData(Long id) {
    return deleteById(CfgRoleDataEntity.class, id, colId);
  }

  @Override
  public List<CfgRoleDataDTO> onSearchExport(CfgRoleDataDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(CfgRoleDataDTO.class));
  }

  public BaseDto sqlSearch(CfgRoleDataDTO cfgRoleDataDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_ROLE_DATA,
            "getListCfgRoleData");
    Map<String, Object> parameters = new HashMap<>();
    if (cfgRoleDataDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(cfgRoleDataDTO.getUsername())) {
        sqlQuery += " AND rd.USERNAME = :username ";
        parameters.put("username", cfgRoleDataDTO.getUsername());
      }
      if (!StringUtils.isStringNullOrEmpty(cfgRoleDataDTO.getSystem())) {
        sqlQuery += " AND rd.SYSTEM = :system ";
        parameters.put("system", cfgRoleDataDTO.getSystem());
      }
      if (!StringUtils.isStringNullOrEmpty(cfgRoleDataDTO.getRole())) {
        sqlQuery += " AND rd.ROLE = :role ";
        parameters.put("role", cfgRoleDataDTO.getRole());
      }
      if (cfgRoleDataDTO.getStatus() != null) {
        if (cfgRoleDataDTO.getStatus() == 1L) {
          sqlQuery += " AND rd.STATUS = :status ";
          parameters.put("status", cfgRoleDataDTO.getStatus());
        } else {
          sqlQuery += " AND (rd.STATUS = :status OR rd.STATUS IS NULL) ";
        }
      }
    }
    sqlQuery += " ORDER BY rd.ID DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public ResultInSideDto checkExisted(String userName, String system, String id) {
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append(
        "Select * from common_gnoc.cfg_role_data C1 WHERE 1=1 AND C1.USERNAME = :user_name AND C1.SYSTEM = :system ");
    parameters.put("user_name", userName);
    parameters.put("system", system);
    List<CfgRoleDataDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CfgRoleDataDTO.class));
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

  public CfgRoleDataDTO checkCreateExit(CfgRoleDataDTO dto) {
    List<CfgRoleDataEntity> dataEntity = (List<CfgRoleDataEntity>) findByMultilParam(
        CfgRoleDataEntity.class,
        "username", dto.getUsername(),
        "system", dto.getSystem(), "role", dto.getRole(), "status", dto.getStatus());
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }
}
