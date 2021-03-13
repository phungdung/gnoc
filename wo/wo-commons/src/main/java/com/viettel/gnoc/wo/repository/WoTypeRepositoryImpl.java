package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.CfgFileCreateWoDTO;
import com.viettel.gnoc.wo.dto.WoTypeFilesGuideDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeTimeDTO;
import com.viettel.gnoc.wo.model.CfgFileCreateWoEntity;
import com.viettel.gnoc.wo.model.WoTypeEntity;
import com.viettel.gnoc.wo.model.WoTypeFilesGuideEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoTypeRepositoryImpl extends BaseRepository implements WoTypeRepository {

  private final static String DATA_EXPORT = "DATA_EXPORT";

  @Override
  public Datatable getListWoTypeByLocalePage(WoTypeInsideDTO woTypeInsideDTO) {
    BaseDto baseDto = sqlSearch(woTypeInsideDTO, null);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        woTypeInsideDTO.getPage(), woTypeInsideDTO.getPageSize(),
        WoTypeInsideDTO.class,
        woTypeInsideDTO.getSortName(), woTypeInsideDTO.getSortType());
  }

  @Override
  public ResultInSideDto delete(Long woTypeId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
//    Map<String, Object> params = new HashMap<>();
//    String sql = " DELETE FROM WFM.WO_TYPE WHERE WO_TYPE_ID = :p_wo_type_id ";
//    params.put("p_wo_type_id", woTypeId);
//    getNamedParameterJdbcTemplate().update(sql, params);
//    deleteByMultilParam(WoTypeEntity.class, "woTypeId", woTypeId);
    WoTypeEntity woTypeEntity = getEntityManager().find(WoTypeEntity.class, woTypeId);
    deleteByMultilParam(WoTypeEntity.class, "woTypeCode", woTypeEntity.getWoTypeCode());
//    getEntityManager().remove(woTypeEntity);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertListWoType(List<WoTypeInsideDTO> woTypeInsideDTOList) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (WoTypeInsideDTO woTypeInsideDTO : woTypeInsideDTOList) {
      if ("1".equals(woTypeInsideDTO.getValidate())) {
        WoTypeInsideDTO woTypeInsideDTOTmp = checkWoTypeExist(woTypeInsideDTO.getWoTypeCode());
        if (woTypeInsideDTOTmp != null) {
          woTypeInsideDTO.setWoTypeId(woTypeInsideDTOTmp.getWoTypeId());
        }
      }
      resultInSideDto = add(woTypeInsideDTO);
    }
    return resultInSideDto;
  }

  @Override
  public Datatable getListDataExport(WoTypeInsideDTO woTypeInsideDTO) {
    BaseDto baseDto = sqlSearch(woTypeInsideDTO, DATA_EXPORT);
    Datatable datatable = new Datatable();
    List<WoTypeInsideDTO> woTypeInsideDTOList = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(WoTypeInsideDTO.class));

    datatable.setData(woTypeInsideDTOList);
    return datatable;
  }

  @Override
  public ResultInSideDto add(WoTypeInsideDTO woTypeInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoTypeEntity woTypeEntity = getEntityManager().merge(woTypeInsideDTO.toEntity());
    resultInSideDto.setId(woTypeEntity.getWoTypeId());
    return resultInSideDto;
  }

  @Override
  public WoTypeInsideDTO checkWoTypeExist(String woTypeCode) {
    List<WoTypeEntity> dataEntity = (List<WoTypeEntity>) findByMultilParam(
        WoTypeEntity.class,
        "woTypeCode", woTypeCode);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public WoTypeInsideDTO findByWoTypeId(Long woTypeDTOId) {
    WoTypeEntity woTypeEntity = getEntityManager().find(WoTypeEntity.class, woTypeDTOId);
    WoTypeInsideDTO woType = woTypeEntity.toDTO();
    return woType;
  }

  @Override
  public List<WoTypeInsideDTO> getListWoTypeForWoCdGroup(List<Long> listWoTypeId) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE, "get-List-Wo-Type-For-Wo-Cd-Group");
    Map<String, Object> parameters = new HashMap<>();
    String leeLocale = I18n.getLocale();
    parameters.put("leeLocale", leeLocale);
    if (listWoTypeId != null && !listWoTypeId.isEmpty()) {
      sql += " AND t.WO_TYPE_ID IN (:listWoTypeId)";
      parameters.put("listWoTypeId", listWoTypeId);
    }
    sql += " AND t.IS_ENABLE = 1";
    sql += " ORDER BY NLSSORT(t.WO_TYPE_NAME,'NLS_SORT=vietnamese')";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoTypeInsideDTO.class));
  }

  @Override
  public List<WoTypeInsideDTO> getListWoTypeByWoTypeName(String woTypeName) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE, "get-List-Wo-Type-For-Wo-Cd-Group");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(woTypeName)) {
      sql += " AND t.WO_TYPE_NAME = :woTypeName";
      parameters.put("woTypeName", woTypeName);
    }
    sql += " ORDER BY NLSSORT(t.WO_TYPE_NAME,'NLS_SORT=vietnamese')";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoTypeInsideDTO.class));
  }

  @Override
  public List<WoTypeTimeDTO> getListWoTypeTimeDTO(WoTypeTimeDTO woTypeTimeDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE, "get-List-Wo-Type-Time-DTO");
    Map<String, Object> parameters = new HashMap<>();
    if (woTypeTimeDTO.getWoTypeTimeId() != null) {
      sql += " and a.wo_type_time_id = :woTypeTimeId";
      parameters.put("woTypeTimeId", woTypeTimeDTO.getWoTypeTimeId());
    }
    if (woTypeTimeDTO.getWoTypeId() != null) {
      sql += " and a.wo_type_id = :woTypeId";
      parameters.put("woTypeId", woTypeTimeDTO.getWoTypeId());
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoTypeTimeDTO.class));
  }

  @Override
  public List<WoTypeInsideDTO> getListWoTypeIsEnable(WoTypeInsideDTO woTypeInsideDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE, "get-List-Wo-Type-Is-Enable");
    Map<String, Object> parameters = new HashMap<>();
    String leeLocale = I18n.getLocale();
    parameters.put("leeLocale", leeLocale);
    if (woTypeInsideDTO != null) {
      if (woTypeInsideDTO.getWoTypeId() != null) {
        sql += " AND w.wo_type_id = :woTypeId";
        parameters.put("woTypeId", woTypeInsideDTO.getWoTypeId());
      }
      if (StringUtils.isNotNullOrEmpty(woTypeInsideDTO.getWoTypeCode())) {
        sql += " AND w.wo_type_code = :woTypeCode";
        parameters.put("woTypeCode", woTypeInsideDTO.getWoTypeCode());
      }
      if (StringUtils.isNotNullOrEmpty(woTypeInsideDTO.getWoTypeName())) {
        sql += " AND w.wo_type_name = :woTypeName";
        parameters.put("woTypeName", woTypeInsideDTO.getWoTypeName());
      }
      if (woTypeInsideDTO.getWoGroupType() != null) {
        if (!"-1".equals(String.valueOf(woTypeInsideDTO.getWoGroupType()))) {
          sql += " AND w.wo_group_type = :woGroupType";
          parameters.put("woGroupType", woTypeInsideDTO.getWoGroupType());
        } else {
          sql += " AND w.wo_group_type is null";
        }
      }
      if (woTypeInsideDTO.getEnableCreate() != null) {
        if ("1".equals(String.valueOf(woTypeInsideDTO.getEnableCreate()))) {
          sql += " AND (w.enable_create is null or w.enable_create = 1)";
        } else {
          sql += " AND w.enable_create = 0";
        }
      }
      if (woTypeInsideDTO.getIsOtherSys() != null && woTypeInsideDTO.getIsOtherSys()) {
        sql += " AND (w.create_from_other_sys is null or w.create_from_other_sys = 1)";
      }
      if (woTypeInsideDTO.getCreateFromOtherSys() != null) {
        if ("1".equals(String.valueOf(woTypeInsideDTO.getCreateFromOtherSys()))) {
          sql += " AND (w.create_from_other_sys is null or w.create_from_other_sys = 1)";
        } else {
          sql += " AND w.create_from_other_sys = 0";
        }
      }
      if (woTypeInsideDTO.getLstCdGroup() != null && woTypeInsideDTO.getLstCdGroup().size() > 0) {
        sql += " and w.wo_type_id in (select distinct(k.wo_type_id)"
            + " from wfm.wo_type_group k"
            + " where k.wo_group_id in (:lstCdGroup))";
        parameters.put("lstCdGroup", woTypeInsideDTO.getLstCdGroup());
      }
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoTypeInsideDTO.class));
  }

  @Override
  public Datatable getListWoTypeIsEnableDataTable(WoTypeInsideDTO woTypeInsideDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE, "get-List-Wo-Type-Is-Enable");
    Map<String, Object> parameters = new HashMap<>();
    String leeLocale = I18n.getLocale();
    parameters.put("leeLocale", leeLocale);
    if (woTypeInsideDTO != null) {
      if (woTypeInsideDTO.getWoTypeId() != null) {
        sql += " AND w.wo_type_id = :woTypeId";
        parameters.put("woTypeId", woTypeInsideDTO.getWoTypeId());
      }
      if (StringUtils.isNotNullOrEmpty(woTypeInsideDTO.getWoTypeCode())) {
        sql += " AND w.wo_type_code = :woTypeCode";
        parameters.put("woTypeCode", woTypeInsideDTO.getWoTypeCode());
      }
      if (StringUtils.isNotNullOrEmpty(woTypeInsideDTO.getWoTypeName())) {
        sql += " AND lower(w.wo_type_name) LIKE '%' || lower(:woTypeName) || '%' ESCAPE '\\'";
        parameters.put("woTypeName", woTypeInsideDTO.getWoTypeName());
      }
      if (woTypeInsideDTO.getWoGroupType() != null) {
        if (!"-1".equals(String.valueOf(woTypeInsideDTO.getWoGroupType()))) {
          sql += " AND w.wo_group_type = :woGroupType";
          parameters.put("woGroupType", woTypeInsideDTO.getWoGroupType());
        } else {
          sql += " AND w.wo_group_type is null";
        }
      }
      if (woTypeInsideDTO.getEnableCreate() != null) {
        if ("1".equals(String.valueOf(woTypeInsideDTO.getEnableCreate()))) {
          sql += " AND (w.enable_create is null or w.enable_create = 1)";
        } else {
          sql += " AND w.enable_create = 0";
        }
      }
      if (woTypeInsideDTO.getIsOtherSys() != null && woTypeInsideDTO.getIsOtherSys()) {
        sql += " AND (w.create_from_other_sys is null or w.create_from_other_sys = 1)";
      }
      if (woTypeInsideDTO.getCreateFromOtherSys() != null) {
        if ("1".equals(String.valueOf(woTypeInsideDTO.getCreateFromOtherSys()))) {
          sql += " AND (w.create_from_other_sys is null or w.create_from_other_sys = 1)";
        } else {
          sql += " AND w.create_from_other_sys = 0";
        }
      }
      if (woTypeInsideDTO.getLstCdGroup() != null && woTypeInsideDTO.getLstCdGroup().size() > 0) {
        sql += " and w.wo_type_id in (select distinct(k.wo_type_id)"
            + " from wfm.wo_type_group k"
            + " where k.wo_group_id in (:lstCdGroup))";
        parameters.put("lstCdGroup", woTypeInsideDTO.getLstCdGroup());
      }
    }
    return getListDataTableBySqlQuery(sql,
        parameters,
        woTypeInsideDTO.getPage(), woTypeInsideDTO.getPageSize(),
        WoTypeInsideDTO.class,
        woTypeInsideDTO.getSortName(), woTypeInsideDTO.getSortType());
  }

  public BaseDto sqlSearch(WoTypeInsideDTO woTypeInsideDTO, String export) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery;
    if (DATA_EXPORT.equals(export)) {
      sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE, "wo-Type-export");
    } else {
      sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE, "wo-Type");
    }
    Map<String, Object> parameters = new HashMap<>();
    if (woTypeInsideDTO != null) {
      if (StringUtils.isNotNullOrEmpty(woTypeInsideDTO.getSearchAll())) {
        sqlQuery += " AND (lower(DECODE(lex.LEE_VALUE, null , w.wo_type_name, lex.LEE_VALUE)) LIKE :searchAll ESCAPE '\\' OR lower(w.wo_type_code) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(woTypeInsideDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(woTypeInsideDTO.getWoTypeId())) {
        sqlQuery += " AND w.wo_type_id= :woTypeId ";
        parameters.put("woTypeId", woTypeInsideDTO.getWoTypeId());
      }
      if (!StringUtils.isStringNullOrEmpty(woTypeInsideDTO.getWoTypeCode())) {
        sqlQuery += " AND lower(w.wo_type_code) LIKE :woTypeCode ESCAPE '\\' ";
        parameters
            .put("woTypeCode",
                StringUtils.convertLowerParamContains(woTypeInsideDTO.getWoTypeCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(woTypeInsideDTO.getWoTypeName())) {
        sqlQuery += " AND (lower(DECODE(lex.LEE_VALUE, null , w.wo_type_name, lex.LEE_VALUE)) LIKE :woTypeName ESCAPE '\\' )";
        parameters
            .put("woTypeName",
                StringUtils.convertLowerParamContains(woTypeInsideDTO.getWoTypeName()));
      }
      if (!StringUtils.isStringNullOrEmpty(woTypeInsideDTO.getIsEnable())) {
        sqlQuery += " AND w.is_enable= :isEnable ";
        parameters.put("isEnable", woTypeInsideDTO.getIsEnable());
      }
      if (!StringUtils.isStringNullOrEmpty(woTypeInsideDTO.getCreateFromOtherSys())) {
        sqlQuery += " AND w.create_from_other_sys= :createFromOtherSys ";
        parameters.put("createFromOtherSys", woTypeInsideDTO.getCreateFromOtherSys());
      }
      if (!StringUtils.isStringNullOrEmpty(woTypeInsideDTO.getAllowPending())) {
        sqlQuery += " AND w.allow_pending= :allowPending ";
        parameters.put("allowPending", woTypeInsideDTO.getAllowPending());
      }
      if (!StringUtils.isStringNullOrEmpty(woTypeInsideDTO.getEnableCreate())) {
        sqlQuery += " AND w.enable_Create= :enableCreate ";
        parameters.put("enableCreate", woTypeInsideDTO.getEnableCreate());
      }
      if (!StringUtils.isStringNullOrEmpty(woTypeInsideDTO.getWoGroupType())) {
        sqlQuery += " AND w.wo_group_type= :woGroupType ";
        parameters.put("woGroupType", woTypeInsideDTO.getWoGroupType());
      }
      parameters.put("p_leeLocale", I18n.getLocale());
      parameters.put("locale", I18n.getLocaleSplit());
    }
    sqlQuery += " order by NLSSORT(w.wo_type_name,'NLS_SORT=vietnamese') ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public List<WoTypeInsideDTO> getListWoTypeByLocaleNotLike(WoTypeInsideDTO woTypeInsideDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE, "get-list-wo-type-by-locale-not-like");
    Map<String, Object> parameters = new HashMap<>();
    if (woTypeInsideDTO != null && !StringUtils
        .isStringNullOrEmpty(woTypeInsideDTO.getWoTypeId())) {
      sql += " AND w.wo_type_id = :woTypeId ";
      parameters.put("woTypeId", woTypeInsideDTO.getWoTypeId());
    }

    if (woTypeInsideDTO != null && !StringUtils
        .isStringNullOrEmpty(woTypeInsideDTO.getWoTypeCode())) {
      sql += " AND lower(w.wo_type_code) = :woTypeCode ";
      parameters.put("woTypeCode", woTypeInsideDTO.getWoTypeCode().trim().toLowerCase());
    }

    if (woTypeInsideDTO != null && !StringUtils
        .isStringNullOrEmpty(woTypeInsideDTO.getWoTypeName())) {
      sql += " AND lower(DECODE(l.wo_type_name, null , w.wo_type_name, l.wo_type_name)) like (:woTypeName) escape '\\' ";
      parameters.put("woTypeName",
          StringUtils.convertLowerParamContains(woTypeInsideDTO.getWoTypeName()));
    }

    if (woTypeInsideDTO != null && !StringUtils
        .isStringNullOrEmpty(woTypeInsideDTO.getCdGroupId())) {
      sql += " AND w.wo_type_id in (select wo_type_id from wo_type_group where wo_group_id = :cdGroupId ) ";
      parameters.put("cdGroupId", woTypeInsideDTO.getCdGroupId());
    }

    if (!StringUtils.isStringNullOrEmpty(I18n.getLocale())) {
      sql += " AND l.locale_code(+) = :locale ";
      parameters.put("locale", I18n.getLocale());
    }

    if (woTypeInsideDTO != null && !StringUtils
        .isStringNullOrEmpty(woTypeInsideDTO.getWoGroupType())) {
      sql += " AND w.wo_group_type  = :woGroupType  ";
      parameters.put("woGroupType", woTypeInsideDTO.getWoGroupType());
    }

    if (woTypeInsideDTO != null && !StringUtils
        .isStringNullOrEmpty(woTypeInsideDTO.getIsEnable())) {
      sql += " AND w.is_enable = :isEnable ";
      parameters.put("isEnable", woTypeInsideDTO.getIsEnable());
    }

    sql += " order by NLSSORT(w.wo_type_name,'NLS_SORT=vietnamese') ";

    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoTypeInsideDTO.class));
  }

  @Override
  public List<WoTypeInsideDTO> getListWoTypeByWoGroup(Long cdGroupId, String system,
      String locale) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE, "get-List-Wo-Type-By-Wo-Group");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(cdGroupId)) {
      sql += " AND wg.wo_group_id =:p_wo_group_id  ";
      parameters.put("p_wo_group_id", cdGroupId);
    }
    if (!StringUtils.isStringNullOrEmpty(system)) {
      sql += " AND wg.wo_group_id =:p_wo_group_id  ";
      parameters.put("p_wo_group_id", system);
    }
    if (!StringUtils.isStringNullOrEmpty(locale)) {
      sql += " AND l.locale_code(+) =:p_locale ";
      parameters.put("p_locale", locale);
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoTypeInsideDTO.class));
  }

  @Override
  public List<WoTypeInsideDTO> getListWoTypeByLocale(
      WoTypeInsideDTO woTypeDTO, String locale) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE, "get-List-Wo-Type-By-Locale");
    Map<String, Object> parameters = new HashMap<>();
    if (woTypeDTO != null && !StringUtils.isStringNullOrEmpty(woTypeDTO.getWoTypeId())) {
      sql += " AND w.wo_type_id =:p_wo_type_id ";
      parameters.put("p_wo_type_id", woTypeDTO.getWoTypeId());
    }

    if (woTypeDTO != null && !StringUtils.isStringNullOrEmpty(woTypeDTO.getWoTypeCode())) {
      sql += " AND lower(w.wo_type_code) like :p_wo_type_code escape '\\' ";
      parameters.put("p_wo_type_code",
          "%" + woTypeDTO.getWoTypeCode().trim().toLowerCase().replace("\\", "\\\\\\")
              .replaceAll("_", "\\\\_")
              .replaceAll("%", "\\\\%") + "%");
    }

    if (woTypeDTO != null && !StringUtils.isStringNullOrEmpty(woTypeDTO.getWoTypeName())) {
      sql += " AND lower(DECODE(l.wo_type_name, null , w.wo_type_name, l.wo_type_name)) like :p_wo_type_name escape '\\' ";
      parameters.put("p_wo_type_name",
          "%" + woTypeDTO.getWoTypeName().trim().toLowerCase().replace("\\", "\\\\\\")
              .replaceAll("_", "\\\\_")
              .replaceAll("%", "\\\\%") + "%");
    }

    if (woTypeDTO != null && !StringUtils.isStringNullOrEmpty(woTypeDTO.getCdGroupId())) {
      sql += " AND w.wo_type_id in (select wo_type_id from wo_type_group where wo_group_id =:p_wo_group_id ) ";
      parameters.put("p_wo_group_id", Long.valueOf(woTypeDTO.getCdGroupId()));
    }

    if (!StringUtils.isStringNullOrEmpty(locale)) {
      sql += " AND l.locale_code(+) =:p_locale ";
      parameters.put("p_locale", locale);
    }

    if (woTypeDTO != null && !StringUtils.isStringNullOrEmpty(woTypeDTO.getWoGroupType())) {
      sql += " AND w.wo_group_type  =:p_wo_group_type ";
      parameters.put("p_wo_group_type", Long.valueOf(woTypeDTO.getWoGroupType()));
    }

    if (woTypeDTO != null && !StringUtils.isStringNullOrEmpty(woTypeDTO.getIsEnable())) {
      sql += " AND w.is_enable =:p_is_enable ";
      parameters.put("p_is_enable", Long.valueOf(woTypeDTO.getIsEnable()));
    }
    sql += " order by NLSSORT(w.wo_type_name,'NLS_SORT=vietnamese') ";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(WoTypeInsideDTO.class));
  }

  @Override
  public WoTypeInsideDTO getWoTypeByCode(String woTypeCode) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE, "get-Wo-Type-By-Code");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_type_code", woTypeCode);
    List<WoTypeInsideDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(WoTypeInsideDTO.class));
    return lst.isEmpty() ? null : lst.get(0);
  }

  @Override
  public List<LanguageExchangeDTO> getLanguageExchangeWoType(String system, String business) {
    return getLanguageExchange(system, business);
  }

  @Override
  public ResultInSideDto addWoTypeFilesGuide(WoTypeFilesGuideDTO woTypeFilesGuideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoTypeFilesGuideEntity woTypeFilesGuideEntity = getEntityManager()
        .merge(woTypeFilesGuideDTO.toEntity());
    resultInSideDto.setId(woTypeFilesGuideEntity.getWoTypeFilesGuideId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteWoTypeFilesGuide(Long woTypeFilesGuideId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoTypeFilesGuideEntity woTypeFilesGuideEntity = getEntityManager()
        .find(WoTypeFilesGuideEntity.class, woTypeFilesGuideId);
    getEntityManager().remove(woTypeFilesGuideEntity);
    return resultInSideDto;
  }

  @Override
  public List<WoTypeFilesGuideEntity> getListFilesGuideByWoTypeId(Long woTypeId) {
    return findByMultilParam(WoTypeFilesGuideEntity.class, "woTypeId", woTypeId);
  }

  @Override
  public ResultInSideDto addCfgFileCreateWo(CfgFileCreateWoDTO cfgFileCreateWoDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgFileCreateWoEntity cfgFileCreateWoEntity = getEntityManager()
        .merge(cfgFileCreateWoDTO.toEntity());
    resultInSideDto.setId(cfgFileCreateWoEntity.getCfgFileCreateWoId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteCfgFileCreateWo(Long cfgFileCreateWoId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgFileCreateWoEntity cfgFileCreateWoEntity = getEntityManager()
        .find(CfgFileCreateWoEntity.class, cfgFileCreateWoId);
    if (cfgFileCreateWoEntity != null) {
      getEntityManager().remove(cfgFileCreateWoEntity);
    }
    return resultInSideDto;
  }

  @Override
  public List<CfgFileCreateWoEntity> getListFileCreateByWoTypeId(Long woTypeId) {
    return findByMultilParam(CfgFileCreateWoEntity.class, "woTypeId", woTypeId);
  }

  @Override
  public List<WoTypeFilesGuideDTO> getListWoTypeFilesGuideDTO(
      WoTypeFilesGuideDTO woTypeFilesGuideDTO) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE, "get-List-Wo-Type-Files-Guide");
    if (woTypeFilesGuideDTO.getWoTypeFilesGuideId() != null && !""
        .equals(woTypeFilesGuideDTO.getWoTypeFilesGuideId())) {
      sql += " and a.wo_type_files_guide_id= :woTypeFilesGuideId ";
      params.put("woTypeFilesGuideId", woTypeFilesGuideDTO.getWoTypeFilesGuideId());
    }
    if (woTypeFilesGuideDTO.getWoTypeId() != null && !""
        .equals(woTypeFilesGuideDTO.getWoTypeId())) {
      sql += " and a.wo_type_id= :woTypeId ";
      params.put("woTypeId", woTypeFilesGuideDTO.getWoTypeId());
    }
    if (woTypeFilesGuideDTO.getFileName() != null && !""
        .equals(woTypeFilesGuideDTO.getFileName())) {
      sql += " and a.file_name= :fileName ";
      params.put("fileName", woTypeFilesGuideDTO.getFileName());
    }
    if (woTypeFilesGuideDTO.getFilePath() != null && !""
        .equals(woTypeFilesGuideDTO.getFilePath())) {
      sql += " and a.file_path= :filePath ";
      params.put("filePath", woTypeFilesGuideDTO.getFilePath());
    }
    List<WoTypeFilesGuideDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(WoTypeFilesGuideDTO.class));
    return list;
  }
}
