package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgTimeTroubleProcessRepositoryImpl extends BaseRepository implements
    CfgTimeTroubleProcessRepository {


  @Override
  public Datatable getListCfgTimeTroubleProcessDTO(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    BaseDto baseDto = sqlGetListCfgTimeTroubleProcessDTO(cfgTimeTroubleProcessDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        cfgTimeTroubleProcessDTO.getPage(), cfgTimeTroubleProcessDTO.getPageSize(),
        CfgTimeTroubleProcessDTO.class, cfgTimeTroubleProcessDTO.getSortName(),
        cfgTimeTroubleProcessDTO.getSortType());
    return datatable;
  }

  public BaseDto sqlGetListCfgTimeTroubleProcessDTO(
      CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO) {
    BaseDto baseDto = new BaseDto();
    String leeLocale = I18n.getLocale();
    String sql;
    Map<String, Object> parameters = new HashMap<>();
    if ("vi_VN".equals(leeLocale)) {
      sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_TIME_TROUBLE_PROCESS,
          "get-List-Cfg-Time-Trouble-Process-DTO");
    } else {
      sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_TIME_TROUBLE_PROCESS,
          "sql-Get-List-Cfg-Time-Trouble-Process-DTO-By-Language");
      parameters.put("appliedSystem", LANGUAGUE_EXCHANGE_SYSTEM.COMMON);
      parameters.put("appliedBussiness", APPLIED_BUSSINESS.CAT_ITEM.toString());
      parameters.put("leeLocale", leeLocale);
    }
    if (StringUtils.isNotNullOrEmpty(cfgTimeTroubleProcessDTO.getSearchAll())) {
      if ("vi_VN".equals(leeLocale)) {
        sql += " AND (LOWER(ci4.ITEM_NAME) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(ci1.ITEM_NAME) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(ci2.ITEM_NAME) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(ci3.ITEM_NAME) LIKE :searchAll ESCAPE '\\')";
      } else {
        sql += " AND (LOWER(lle4.LEE_VALUE) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(lle1.LEE_VALUE) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(lle2.LEE_VALUE) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(lle3.LEE_VALUE) LIKE :searchAll ESCAPE '\\')";
      }
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(cfgTimeTroubleProcessDTO.getSearchAll()));
    }
    if (StringUtils.isNotNullOrEmpty(cfgTimeTroubleProcessDTO.getCountry())) {
      sql += " AND cfg.COUNTRY = :country";
      parameters.put("country", cfgTimeTroubleProcessDTO.getCountry());
    }
    if (cfgTimeTroubleProcessDTO.getTypeId() != null) {
      sql += " AND cfg.TYPE_ID = :typeId";
      parameters.put("typeId", cfgTimeTroubleProcessDTO.getTypeId());
    }
    if (cfgTimeTroubleProcessDTO.getSubCategoryId() != null) {
      sql += " AND cfg.SUB_CATEGORY_ID = :subCategoryId";
      parameters.put("subCategoryId", cfgTimeTroubleProcessDTO.getSubCategoryId());
    }
    if (cfgTimeTroubleProcessDTO.getPriorityId() != null) {
      sql += " AND cfg.PRIORITY_ID = :priorityId";
      parameters.put("priorityId", cfgTimeTroubleProcessDTO.getPriorityId());
    }
    if (cfgTimeTroubleProcessDTO.getIsCall() != null) {
      sql += " AND cfg.IS_CALL = :isCall";
      parameters.put("isCall", cfgTimeTroubleProcessDTO.getIsCall());
    }
    sql += " ORDER BY cfg.LAST_UPDATE_TIME DESC";
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
