package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrMaterialDTO;
import com.viettel.gnoc.maintenance.dto.MrMaterialDisplacementDTO;
import com.viettel.gnoc.maintenance.model.MrMaterialEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrMaterialDisplacementRepositoryImpl extends BaseRepository implements
    MrMaterialDisplacementRepository {


  @Override
  public List<MrMaterialDisplacementDTO> getListMrMaterialDisplacementDTO(
      MrMaterialDisplacementDTO mrMaterialDisplacementDTO) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_MATERIAL_DISPLACEMENT,
            "getListMrMaterialDisplacementDTO");
    Map<String, Object> parameters = new HashMap<>();
    if (mrMaterialDisplacementDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(mrMaterialDisplacementDTO.getWoId())) {
        sqlQuery += " AND m.WO_ID = :woId ";
        parameters.put("woId", mrMaterialDisplacementDTO.getWoId());
      }
    }
    sqlQuery += " ORDER BY m.MATERIAL_ID DESC ";
    return getNamedParameterJdbcTemplate().query(sqlQuery, parameters,
        BeanPropertyRowMapper.newInstance(MrMaterialDisplacementDTO.class));
  }

  @Override
  public BaseDto sqlSearch(MrMaterialDTO mrMaterialDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_MATERIAL_DISPLACEMENT,
            "getListMrMaterialDTO2");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    if (StringUtils.isNotNullOrEmpty(mrMaterialDTO.getSearchAll())) {
      sql += (" AND ( LOWER(MATERIAL_NAME) LIKE :searchAll ESCAPE '\\' OR LOWER(SERIAL) LIKE :searchAll ESCAPE '\\' ");
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(mrMaterialDTO.getSearchAll()));
      if (I18n.getLanguage(Constants.DEVICE_TYPE_MAP_MULTI_LANG.getDeviceName().get("DH"))
          .toLowerCase().contains(mrMaterialDTO.getSearchAll().toLowerCase().trim())) {
        sql += (" OR DEVICE_TYPE = 'DH' ");
      }
      if (I18n.getLanguage(Constants.DEVICE_TYPE_MAP_MULTI_LANG.getDeviceName().get("MPD"))
          .toLowerCase().contains(mrMaterialDTO.getSearchAll().toLowerCase().trim())) {
        sql += (" OR DEVICE_TYPE = 'MPD' ");
      }
      sql += " ) ";
    }
    if (StringUtils.isNotNullOrEmpty(mrMaterialDTO.getMaterialName())) {
      sql += (" AND LOWER(MATERIAL_NAME) LIKE :materialName ESCAPE '\\' ");
      parameters.put("materialName",
          StringUtils.convertLowerParamContains(mrMaterialDTO.getMaterialName()));
    }
    if (StringUtils.isNotNullOrEmpty(mrMaterialDTO.getSerial())) {
      sql += (" AND LOWER(SERIAL) LIKE :serial ESCAPE '\\' ");
      parameters.put("serial",
          StringUtils.convertLowerParamContains(mrMaterialDTO.getSerial()));
    }
    if (!StringUtils.isStringNullOrEmpty(mrMaterialDTO.getDateTime())) {
      sql += (" AND TO_DATE(TO_CHAR(DATE_TIME, 'MM/YYYY'), 'MM/YYYY') =TO_DATE(:dateTime,'MM/YYYY')");
      parameters.put("dateTime", DateUtil.date2MMyyString(mrMaterialDTO.getDateTime()));
    }
    if (!StringUtils.isStringNullOrEmpty(mrMaterialDTO.getDateTimeFromSearch())) {
      sql += (" AND TO_DATE(TO_CHAR(DATE_TIME, 'MM/YYYY'),'MM/YYYY') >= TO_DATE(:dateTimeFromSearch,'MM/YYYY') ");
      parameters.put("dateTimeFromSearch",
          DateUtil.date2MMyyString(mrMaterialDTO.getDateTimeFromSearch()));
    }
    if (!StringUtils.isStringNullOrEmpty(mrMaterialDTO.getDateTimeToSearch())) {
      sql += (" AND  TO_DATE(TO_CHAR(DATE_TIME, 'MM/YYYY'), 'MM/YYYY') <= TO_DATE(:dateTimeToSearch, 'MM/YYYY') ");
      parameters
          .put("dateTimeToSearch", DateUtil.date2MMyyString(mrMaterialDTO.getDateTimeToSearch()));
    }
    if (!StringUtils.isStringNullOrEmpty(mrMaterialDTO.getMaterialId())) {
      sql += (" AND MATERIAL_ID =:materialId");
      parameters.put("materialId", mrMaterialDTO.getMaterialId());
    }
    if (!StringUtils.isStringNullOrEmpty(mrMaterialDTO.getMarketCode())) {
      sql += (" AND MARKET_CODE = :marketCode ");
      parameters.put("marketCode", mrMaterialDTO.getMarketCode());
    }
    if (StringUtils.isNotNullOrEmpty(mrMaterialDTO.getDeviceType())) {
      if (I18n.getLanguage(Constants.DEVICE_TYPE_MAP_MULTI_LANG.getDeviceName().get("DH"))
          .toLowerCase().contains(mrMaterialDTO.getDeviceType().toLowerCase().trim())) {
        sql += (" AND DEVICE_TYPE = 'DH' ");
      }
      if (I18n.getLanguage(Constants.DEVICE_TYPE_MAP_MULTI_LANG.getDeviceName().get("MPD"))
          .toLowerCase().contains(mrMaterialDTO.getDeviceType().toLowerCase().trim())) {
        sql += (" AND DEVICE_TYPE = 'MPD' ");
      }
    }
    if (!StringUtils.isStringNullOrEmpty(mrMaterialDTO.getUnitPrice())) {
      sql += (" AND UNIT_PRICE = :unitPrice ");
      parameters.put("unitPrice", mrMaterialDTO.getUnitPrice());
    }
    sql += (" ORDER BY MATERIAL_ID DESC ");
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public Datatable getListMrMaterialDTO2(MrMaterialDTO mrMaterialDTO) {
    BaseDto baseDto = sqlSearch(mrMaterialDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrMaterialDTO.getPage(), mrMaterialDTO.getPageSize(), MrMaterialDTO.class,
        mrMaterialDTO.getSortName(), mrMaterialDTO.getSortType());
  }

  @Override
  public MrMaterialDTO getDetail(Long id) {
    if (!StringUtils.isStringNullOrEmpty(id)) {
      BaseDto baseDto = sqlSearch(new MrMaterialDTO(id));
      List<MrMaterialDTO> lst = getNamedParameterJdbcTemplate()
          .query(baseDto.getSqlQuery(), baseDto.getParameters(),
              BeanPropertyRowMapper.newInstance(MrMaterialDTO.class));
      return lst.isEmpty() ? null : lst.get(0);
    }
    return null;
  }

  @Override
  public List<MrMaterialDTO> getDataExport(MrMaterialDTO mrMaterialDTO) {
    BaseDto baseDto = sqlSearch(mrMaterialDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrMaterialDTO.class));
  }

  @Override
  public ResultInSideDto insertOrUpdateMrMaterial(MrMaterialDTO mrMaterialDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrMaterialEntity entity = getEntityManager().merge(mrMaterialDTO.toEntity());
    resultInSideDto.setId(entity.getMaterialId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteMrMaterial(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrMaterialEntity entity = getEntityManager().find(MrMaterialEntity.class, id);
    getEntityManager().remove(entity);
    resultInSideDto.setId(id);
    return resultInSideDto;
  }

  @Override
  public List<MrMaterialDTO> checkListDuplicate(MrMaterialDTO mrMaterialDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_MATERIAL_DISPLACEMENT,
            "checkListDuplicate");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    if (StringUtils.isNotNullOrEmpty(mrMaterialDTO.getMaterialName())) {
      sql += (" AND MATERIAL_NAME =:materialName ");
      parameters.put("materialName", mrMaterialDTO.getMaterialName());
    }
    if (!StringUtils.isStringNullOrEmpty(mrMaterialDTO.getMarketCode())) {
      sql += (" AND MARKET_CODE = :marketCode ");
      parameters.put("marketCode", mrMaterialDTO.getMarketCode());
    }
    if (StringUtils.isNotNullOrEmpty(mrMaterialDTO.getDeviceType())) {
      sql += (" AND DEVICE_TYPE = :deviceType ");
      parameters.put("deviceType", mrMaterialDTO.getDeviceType());
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrMaterialDTO.class));
  }
}
