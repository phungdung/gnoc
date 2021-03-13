package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CfgInfoTtSpmDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.CfgInfoTtSpmEntity;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author TienNV
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
public class CfgInfoTtSpmRepositoryImpl extends BaseRepository implements CfgInfoTtSpmRepository {

  @Override
  public List<CfgInfoTtSpmDTO> getListCfgInfoTtSpmDTO(CfgInfoTtSpmDTO cfgInfoTtSpmDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(CfgInfoTtSpmEntity.class, cfgInfoTtSpmDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public String updateCfgInfoTtSpm(CfgInfoTtSpmDTO dto) {
    CfgInfoTtSpmEntity cfgInfoTtSpmEntity = dto.toEntity();
    getEntityManager().merge(cfgInfoTtSpmEntity);
    return RESULT.SUCCESS;
  }

  @Override
  public String deleteCfgInfoTtSpm(Long id) {
    return deleteById(CfgInfoTtSpmEntity.class, id, colId);
  }

  @Override
  public String deleteListCfgInfoTtSpm(List<CfgInfoTtSpmDTO> dto) {
    return deleteByListDTO(dto, CfgInfoTtSpmEntity.class, colId);
  }

  @Override
  public CfgInfoTtSpmDTO findCfgInfoTtSpmById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(CfgInfoTtSpmEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertCfgInfoTtSpm(CfgInfoTtSpmDTO cfgInfoTtSpmDTO) {
    CfgInfoTtSpmEntity entity = cfgInfoTtSpmDTO.toEntity();
    return insertByModel(entity, colId);
  }

  @Override
  public String insertOrUpdateListCfgInfoTtSpm(List<CfgInfoTtSpmDTO> cfgInfoTtSpmDTOS) {
    for (CfgInfoTtSpmDTO dto : cfgInfoTtSpmDTOS) {
      CfgInfoTtSpmEntity entity = dto.toEntity();
      if (entity.getId() != null && entity.getId() > 0) {
        getEntityManager().merge(entity);
      } else {
        getEntityManager().persist(entity);
      }
    }
    return RESULT.SUCCESS;
  }

  @Override
  public List<String> getSequenseCfgInfoTtSpm(String seqName, int... size) {
    return getListSequense(seqName, size);
  }

  @Override
  public List<CfgInfoTtSpmDTO> getListCfgInfoTtSpmByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortName) {
    return onSearchByConditionBean(CfgInfoTtSpmEntity.class, lstCondition, rowStart, maxRow,
        sortType, sortName);
  }

  @Override
  public Datatable getListCfgInfoTtSpmDTO2(CfgInfoTtSpmDTO cfgInfoTtSpmDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_TT_CFG_INFO_SPM, "get_List_Cfg_Info_Tt");
    String locale = I18n.getLocale();
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("leeLocale", locale);
    if (StringUtils.isNotNullOrEmpty(cfgInfoTtSpmDTO.getTroubleName())) {
      sql += "  AND LOWER(list_result.troubleName) LIKE :troubleName ESCAPE '\\'";
      parameters.put("troubleName",
          StringUtils.convertLowerParamContains(cfgInfoTtSpmDTO.getTroubleName()));
    }
    if (!StringUtils.isStringNullOrEmpty(cfgInfoTtSpmDTO.getTypeId())) {
      sql += "  AND list_result.typeId= :typeId ";
      parameters.put("typeId", cfgInfoTtSpmDTO.getTypeId());
    }
    if (!StringUtils.isStringNullOrEmpty(cfgInfoTtSpmDTO.getAlarmGroupId())) {
      sql += " AND list_result.alarmGroupId= :alarmGroupId ";
      parameters.put("alarmGroupId", cfgInfoTtSpmDTO.getAlarmGroupId());
    }
    if (!StringUtils.isStringNullOrEmpty(cfgInfoTtSpmDTO.getSubCategoryId())) {
      sql += " AND list_result.subCategoryId= :subCategoryId ";
      parameters.put("subCategoryId", cfgInfoTtSpmDTO.getSubCategoryId());
    }

    if (StringUtils.isNotNullOrEmpty(cfgInfoTtSpmDTO.getSearchAll())) {
      sql += " AND (LOWER(list_result.troubleName) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(list_result.typeName) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(list_result.alarmGroupName) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(list_result.subCategoryName) LIKE :searchAll ESCAPE '\\')";
      parameters
          .put("searchAll", StringUtils.convertLowerParamContains(cfgInfoTtSpmDTO.getSearchAll()));

    }
    sql += "ORDER BY list_result.typeId";
    return getListDataTableBySqlQuery(sql, parameters, cfgInfoTtSpmDTO.getPage(),
        cfgInfoTtSpmDTO.getPageSize(), CfgInfoTtSpmDTO.class,
        cfgInfoTtSpmDTO.getSortName(),
        cfgInfoTtSpmDTO.getSortType());
  }


  private static final String colId = "id";

}
