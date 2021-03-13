package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CfgBusinessCallSmsDTO;
import com.viettel.gnoc.commons.dto.CfgBusinessCallSmsUserDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.CfgBusinessCallSmsEntity;
import com.viettel.gnoc.commons.model.CfgBusinessCallSmsUserEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.WO_CD_GROUP_MASTER_CODE;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgBusinessCallSmsRepositoryImpl extends BaseRepository implements
    CfgBusinessCallSmsRepository {


  @Override
  public Datatable getListCfgBusinessCallSmsDTO(CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO) {
    BaseDto baseDto = sqlSearch(cfgBusinessCallSmsDTO, 1L);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        cfgBusinessCallSmsDTO.getPage(), cfgBusinessCallSmsDTO.getPageSize(),
        CfgBusinessCallSmsDTO.class,
        cfgBusinessCallSmsDTO.getSortName(), cfgBusinessCallSmsDTO.getSortType());
  }

  @Override
  public ResultInSideDto add(CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgBusinessCallSmsEntity cfgBusinessCallSmsEntity = getEntityManager()
        .merge(cfgBusinessCallSmsDTO.toEntity());
    resultInSideDto.setId(cfgBusinessCallSmsEntity.getId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto edit(CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(cfgBusinessCallSmsDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public CfgBusinessCallSmsDTO getDetail(Long id) {
    CfgBusinessCallSmsEntity cfgBusinessCallSmsEntity = getEntityManager()
        .find(CfgBusinessCallSmsEntity.class, id);
    CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO = cfgBusinessCallSmsEntity.toDTO();
    return cfgBusinessCallSmsDTO;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgBusinessCallSmsEntity cfgBusinessCallSmsEntity = getEntityManager()
        .find(CfgBusinessCallSmsEntity.class, id);
    if (cfgBusinessCallSmsEntity != null) {
      getEntityManager().remove(cfgBusinessCallSmsEntity);
    }
    return resultInSideDto;
  }

  @Override
  public List<CfgBusinessCallSmsDTO> getListDataExport(
      CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO) {
    BaseDto baseDto = sqlSearch(cfgBusinessCallSmsDTO, 0L);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(CfgBusinessCallSmsDTO.class));
  }

  @Override
  public ResultInSideDto addUser(CfgBusinessCallSmsUserDTO cfgBusinessCallSmsUserDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgBusinessCallSmsUserEntity cfgBusinessCallSmsUserEntity = getEntityManager()
        .merge(cfgBusinessCallSmsUserDTO.toEntity());
    resultInSideDto.setId(cfgBusinessCallSmsUserEntity.getId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto editUser(CfgBusinessCallSmsUserDTO cfgBusinessCallSmsUserDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(cfgBusinessCallSmsUserDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public CfgBusinessCallSmsUserDTO ckeckUserExist(Long cfgBusinessId, Long userId) {
    List<CfgBusinessCallSmsUserEntity> lstCallSmsUserEntity = findByMultilParam(
        CfgBusinessCallSmsUserEntity.class, "cfgBusinessId", cfgBusinessId, "userId", userId);
    if (lstCallSmsUserEntity != null && !lstCallSmsUserEntity.isEmpty()) {
      return lstCallSmsUserEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public CfgBusinessCallSmsDTO ckeckCfgBusinessCallSmsExist(Long cfgTypeId, Long cdId,
      Long cfgLevel) {
    List<CfgBusinessCallSmsEntity> lstCallSmsEntity = findByMultilParam(
        CfgBusinessCallSmsEntity.class, "cfgTypeId", cfgTypeId, "cdId", cdId, "cfgLevel", cfgLevel);
    if (lstCallSmsEntity != null && !lstCallSmsEntity.isEmpty()) {
      return lstCallSmsEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteUser(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgBusinessCallSmsUserEntity cfgBusinessCallSmsUserEntity = getEntityManager()
        .find(CfgBusinessCallSmsUserEntity.class, id);
    if (cfgBusinessCallSmsUserEntity != null) {
      getEntityManager().remove(cfgBusinessCallSmsUserEntity);
    }
    return resultInSideDto;
  }

  @Override
  public List<CfgBusinessCallSmsUserDTO> getListUserBycfgBusinessId(Long cfgBusinessId) {
    return findByMultilParam(CfgBusinessCallSmsUserEntity.class, "cfgBusinessId", cfgBusinessId);
  }

  public BaseDto sqlSearch(CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO, Long key) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery;
    if (key == 1L) {
      sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_BUSINESS_CALL_SMS,
              "getListCfgBusinessCallSmsDTO");
    } else {
      sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_BUSINESS_CALL_SMS,
              "getListCfgBusinessCallSmsExport");
    }
    Map<String, Object> parameters = new HashMap<>();
    if (cfgBusinessCallSmsDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(cfgBusinessCallSmsDTO.getCfgTypeId())) {
        sqlQuery += " AND b.cfg_type_id = :cfgTypeId ";
        parameters.put("cfgTypeId", cfgBusinessCallSmsDTO.getCfgTypeId());
      }
      if (!StringUtils.isStringNullOrEmpty(cfgBusinessCallSmsDTO.getCdId())) {
        sqlQuery += " AND b.cd_id = :cdId ";
        parameters.put("cdId", cfgBusinessCallSmsDTO.getCdId());
      }
      if (!StringUtils.isStringNullOrEmpty(cfgBusinessCallSmsDTO.getCfgLevel())) {
        sqlQuery += " AND b.cfg_level = :cfgLevel ";
        parameters.put("cfgLevel", cfgBusinessCallSmsDTO.getCfgLevel());
      }
    }
    sqlQuery += " ORDER BY b.id DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  public List<WoCdGroupInsideDTO> getListWoCdGroupCBB() {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_BUSINESS_CALL_SMS, "getListWoCdGroupDTO");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    parameters.put("categoryCode", WO_CD_GROUP_MASTER_CODE.WO_CD_GROUP_TYPE);
    sql += " ORDER BY g.WO_GROUP_ID DESC";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));
  }
}
