package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureBtsExportDTO;
import com.viettel.gnoc.maintenance.model.MrCfgProcedureBtsEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrCfgProcedureBtsRepositoryImpl extends BaseRepository implements
    MrCfgProcedureBtsRepository {

  @Override
  public Datatable onSearch(MrCfgProcedureBtsDTO mrCfgProcedureBtsDTO) {
    BaseDto baseDto = sqlSearchMrCfgProcedureBts(mrCfgProcedureBtsDTO, false);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrCfgProcedureBtsDTO.getPage(),
        mrCfgProcedureBtsDTO.getPageSize(), MrCfgProcedureBtsDTO.class,
        mrCfgProcedureBtsDTO.getSortName(), mrCfgProcedureBtsDTO.getSortType());
  }

  @Override
  public List<MrCfgProcedureBtsExportDTO> onSearchExport(
      MrCfgProcedureBtsDTO mrCfgProcedureBtsDTO) {
    BaseDto baseDto = sqlSearchMrCfgProcedureBts(mrCfgProcedureBtsDTO, false);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrCfgProcedureBtsExportDTO.class));
  }

  @Override
  public String insertListMrCfgProcedureBts(List<MrCfgProcedureBtsDTO> lstData) {
    if (lstData != null && lstData.size() > 0) {
      for (MrCfgProcedureBtsDTO dto : lstData) {
        insertOrUpdateMrCfgProcedureBts(dto);
      }
      return RESULT.SUCCESS;
    }
    return RESULT.ERROR;
  }

  @Override
  public ResultInSideDto insertOrUpdateMrCfgProcedureBts(MrCfgProcedureBtsDTO dto) {
    MrCfgProcedureBtsEntity entity = dto.toEntity();
    if (dto.getCfgProcedureBtsId() != null) {
      getEntityManager().merge(entity);
    } else {
      getEntityManager().persist(entity);
    }
    return new ResultInSideDto(entity.getCfgProcedureBtsId(), RESULT.SUCCESS, RESULT.SUCCESS);
  }

  @Override
  public MrCfgProcedureBtsDTO findById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(MrCfgProcedureBtsEntity.class, id).toDTO();
    }
    return null;
  }

  public BaseDto sqlSearchMrCfgProcedureBts(MrCfgProcedureBtsDTO dto, boolean isCheckDup) {
    StringBuilder sql = new StringBuilder(
        SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CFG_PROCEDURE_BTS, "on-search")
            .replace("$fuel.type.oil$", I18n.getLanguage("fuel.type.oil"))
            .replace("$fuel.type.gas$", I18n.getLanguage("fuel.type.gas"))
    );
    Map<String, Object> param = new HashMap<>();
    if (dto != null) {
      if (!StringUtils.isStringNullOrEmpty(dto.getMarketCode())) {
        sql.append(" AND T1.MARKET_CODE = :marketCode ");
        param.put("marketCode", dto.getMarketCode());
      }

      if (!StringUtils.isStringNullOrEmpty(dto.getDeviceType())) {
        sql.append(" AND T1.DEVICE_TYPE = :deviceType ");
        param.put("deviceType", dto.getDeviceType());
      }

      if (!StringUtils.isStringNullOrEmpty(dto.getCycle())) {
        sql.append(" AND T1.CYCLE = :cycle ");
        param.put("cycle", dto.getCycle());
      }

      if (StringUtils.isNotNullOrEmpty(dto.getMaterialType())) {
        sql.append(" AND lower(T1.MATERIAL_TYPE) = :material_type ");
        param.put("material_type", dto.getMaterialType().toLowerCase());
      }

      if (!StringUtils.isStringNullOrEmpty(dto.getGenMrBefore())) {
        sql.append(" AND LOWER(TO_CHAR(T1.GEN_MR_BEFORE)) LIKE :getMrBefore ESCAPE '\\' ");
        param.put("getMrBefore",
            StringUtils.convertLowerParamContains(dto.getGenMrBefore().toString()));
      }

      if (!StringUtils.isStringNullOrEmpty(dto.getMrTime())) {
        sql.append(" AND LOWER(TO_CHAR(T1.MR_TIME)) LIKE :mr_time ESCAPE '\\' ");
        param.put("mr_time", StringUtils.convertLowerParamContains(dto.getMrTime().toString()));
      }

      if (StringUtils.isNotNullOrEmpty(dto.getSupplierCode())) {
        sql.append(" AND T1.SUPPLIER_CODE = :supplierCode ");
        param.put("supplierCode", dto.getSupplierCode());
      }

      if (StringUtils.isNotNullOrEmpty(dto.getSearchAll())) {
        sql.append(" AND (lower(T1.MAINTENANCE_HOUR) LIKE :searchAll ESCAPE '\\' ");
        if (I18n.getLanguage("fuel.type.oil").toLowerCase()
            .contains(dto.getSearchAll().toLowerCase())) {
          sql.append(" OR T1.MATERIAL_TYPE = 'D' ");
        }
        if (I18n.getLanguage("fuel.type.gas").toLowerCase()
            .contains(dto.getSearchAll().toLowerCase())) {
          sql.append(" OR T1.MATERIAL_TYPE = 'X' ");
        }
        sql.append(" ) ");
        param.put("searchAll",
            StringUtils.convertLowerParamContains(dto.getSearchAll()));
      }

      if (isCheckDup) {
        if (dto.getCfgProcedureBtsId() != null) {
          sql.append(" AND T1.CFG_PROCEDURE_BTS_ID <> :cfgprocedureId ");
          param.put("cfgprocedureId", dto.getCfgProcedureBtsId());
        }
      }

    }
    sql.append(" ORDER BY CFG_PROCEDURE_BTS_ID DESC");

    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql.toString());
    baseDto.setParameters(param);
    return baseDto;
  }

  @Override
  public List<MrCfgProcedureBtsDTO> checkDupp(MrCfgProcedureBtsDTO mrCfgProcedureBtsDTO) {
    BaseDto baseDto = sqlSearchMrCfgProcedureBts(mrCfgProcedureBtsDTO, true);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrCfgProcedureBtsDTO.class));
  }

  @Override
  public ResultInSideDto deleteById(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrCfgProcedureBtsEntity itemEntity = getEntityManager().find(MrCfgProcedureBtsEntity.class, id);
    if (itemEntity != null) {
      getEntityManager().remove(itemEntity);
    }
    return resultInSideDto;
  }
}
