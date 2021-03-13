package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureCDDTO;
import com.viettel.gnoc.maintenance.model.MrCfgProcedureCDEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrCfgProcedureCDRepositoryImpl extends BaseRepository implements
    MrCfgProcedureCDRepository {

  @Override
  public Datatable onSearch(MrCfgProcedureCDDTO mrCfgProcedureCDDTO) {
    BaseDto baseDto = sqlSearchMrCfgProcedureCD(mrCfgProcedureCDDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrCfgProcedureCDDTO.getPage(),
        mrCfgProcedureCDDTO.getPageSize(), MrCfgProcedureCDDTO.class,
        mrCfgProcedureCDDTO.getSortName(), mrCfgProcedureCDDTO.getSortType());
  }

  public BaseDto sqlSearchMrCfgProcedureCD(MrCfgProcedureCDDTO cfgProcedureDTO) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CFG_PROCEDURE_CD, "on-search");
    Map<String, Object> param = new HashMap<>();
    if (cfgProcedureDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(cfgProcedureDTO.getMarketCode())) {
        sql += " AND UPPER(CP.MARKET_CODE) = :markCode ";
        param.put("markCode",
            StringUtils.replaceSpecicalChracterSQL(cfgProcedureDTO.getMarketCode()).toUpperCase());

      }
      if (!StringUtils.isStringNullOrEmpty(cfgProcedureDTO.getProcedureName())) {
        sql += " AND LOWER(CP.PROCEDURE_NAME) LIKE :procedureName ESCAPE '\\'  ";
        param.put("procedureName",
            StringUtils.convertLowerParamContains(cfgProcedureDTO.getProcedureName()));
      }

      if (StringUtils.isNotNullOrEmpty(cfgProcedureDTO.getMrContentId())) {
        sql += " AND LOWER(CP.MR_CONTENT_ID) LIKE :mrContentId ESCAPE '\\'  ";
        param.put("mrContentId",
            StringUtils.convertLowerParamContains(cfgProcedureDTO.getMrContentId()));
      }

      if (cfgProcedureDTO.getCycle() != null) {
        sql += " AND LOWER(CP.CYCLE) LIKE :pCycle ESCAPE '\\'  ";
        param.put("pCycle",
            StringUtils.convertLowerParamContains(String.valueOf(cfgProcedureDTO.getCycle())));
      }

      if (StringUtils.isNotNullOrEmpty(cfgProcedureDTO.getDeviceType())) {
        sql += " AND LOWER(CP.DEVICE_TYPE) LIKE :deviceType ESCAPE '\\'  ";
        param.put("deviceType",
            StringUtils.convertLowerParamContains(cfgProcedureDTO.getDeviceType()));
      }

      if (StringUtils.isNotNullOrEmpty(cfgProcedureDTO.getMarketName())) {
        sql += " AND LOWER(cl.LOCATION_NAME) like :locationName ESCAPE '\\' ";
        param.put("locationName",
            StringUtils.convertLowerParamContains(cfgProcedureDTO.getMarketName()));
      }

      if (StringUtils.isNotNullOrEmpty(cfgProcedureDTO.getSearchAll())) {
        sql += " AND ( "
            + " LOWER(CP.PROCEDURE_NAME) LIKE :searchAll ESCAPE '\\' "
            + " )";
        param.put("searchAll",
            StringUtils.convertLowerParamContains(cfgProcedureDTO.getSearchAll().trim()));
      }
    }
    sql += " ORDER BY marketCode, deviceType, cycle ";

    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(param);
    return baseDto;
  }

  @Override
  public List<MrCfgProcedureCDDTO> onSearchExport(MrCfgProcedureCDDTO mrCfgProcedureCDDTO) {
    BaseDto baseDto = sqlSearchMrCfgProcedureCD(mrCfgProcedureCDDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrCfgProcedureCDDTO.class));
  }

  @Override
  public ResultInSideDto onInsertOrUpdate(MrCfgProcedureCDDTO mrCfgProcedureCDDTO) {
    MrCfgProcedureCDEntity entity = mrCfgProcedureCDDTO.toEntity();
    if (entity.getProcedureId() != null) {
      getEntityManager().merge(entity);
    } else {
      getEntityManager().persist(entity);
    }
    return new ResultInSideDto(entity.getProcedureId(), RESULT.SUCCESS, RESULT.SUCCESS);
  }

  @Override
  public MrCfgProcedureCDDTO findById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(MrCfgProcedureCDEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteById(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrCfgProcedureCDEntity itemEntity = getEntityManager().find(MrCfgProcedureCDEntity.class, id);
    if (itemEntity != null) {
      getEntityManager().remove(itemEntity);
    }
    return resultInSideDto;
  }
}
