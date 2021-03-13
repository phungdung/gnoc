package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.OD_MASTER_CODE;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.od.dto.OdChangeStatusDTO;
import com.viettel.gnoc.od.dto.OdExportCfgBusinessDTO;
import com.viettel.gnoc.od.model.OdChangeStatusEntity;
import com.viettel.gnoc.od.model.OdTypeEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author TienNV
 */
@Repository
@Slf4j
public class OdChangeStatusRepositoryImpl extends BaseRepository implements
    OdChangeStatusRepository {


  @Override
  public Datatable getListOdCfgBusiness(OdChangeStatusDTO odChangeStatusDTO) {
    BaseDto baseOdChangeStatus = sqlSearch(odChangeStatusDTO, "od-cfg-business");
    String sqlQuery = baseOdChangeStatus.getSqlQuery();
    sqlQuery += " ORDER BY ocs.ID DESC";
    baseOdChangeStatus.setSqlQuery(sqlQuery);
    Datatable datatable = getListDataTableBySqlQuery(baseOdChangeStatus.getSqlQuery(),
        baseOdChangeStatus.getParameters(), odChangeStatusDTO.getPage(),
        odChangeStatusDTO.getPageSize(),
        OdChangeStatusDTO.class, odChangeStatusDTO.getSortName(), odChangeStatusDTO.getSortType());
    // if (odChangeStatusDTO.getOdTypeId() != null &&
    //     (datatable == null || datatable.getData() == null || datatable.getData().size() < 1)) {
    //   odChangeStatusDTO.setOdTypeId(null);
    //   odChangeStatusDTO.setIsDefault(1L);
    //   baseOdChangeStatus = sqlSearch(odChangeStatusDTO, "od-cfg-business");
    //   sqlQuery = baseOdChangeStatus.getSqlQuery();
    //   baseOdChangeStatus.setSqlQuery(sqlQuery);
    //   datatable = getListDataTableBySqlQuery(baseOdChangeStatus.getSqlQuery(),
    //       baseOdChangeStatus.getParameters(), odChangeStatusDTO.getPage(),
    //       odChangeStatusDTO.getPageSize(),
    //       OdChangeStatusDTO.class, odChangeStatusDTO.getSortName(),
    //       odChangeStatusDTO.getSortType());
    // }
    try {
      List<OdChangeStatusDTO> odChangeStatusDTOS = (List<OdChangeStatusDTO>) datatable.getData();
      Map<String, Object> map = DataUtil
          .getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.WO,
              Constants.APPLIED_BUSSINESS.WO_TYPE, LocaleContextHolder.getLocale().getLanguage());
      String sqlLanguage = (String) map.get("sql");
      Map mapParam = (Map) map.get("mapParam");
      List<LanguageExchangeDTO> lstLanguage = getNamedParameterJdbcTemplate()
          .query(sqlLanguage, mapParam,
              BeanPropertyRowMapper.newInstance(LanguageExchangeDTO.class));
      odChangeStatusDTOS = DataUtil
          .setLanguage(odChangeStatusDTOS, lstLanguage, "odChangeStatusId", "odChangeStatusName");
      datatable.setData(odChangeStatusDTOS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return datatable;
  }

  @Override
  public List<OdChangeStatusDTO> search(OdChangeStatusDTO odChangeStatusDTO) {
    String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_OD_CHANGE_STATUS, "search");
    Map<String, Object> parameters = new HashMap<>();

    if (odChangeStatusDTO.getOdTypeId() != null) {
      sqlQuery += " AND oct.OD_TYPE_ID = :odTypeId ";
      parameters.put("odTypeId", odChangeStatusDTO.getOdTypeId().toString());
    }
    if (odChangeStatusDTO.getOldStatus() != null) {
      sqlQuery += " AND oct.OLD_STATUS = :oldStatus ";
      parameters
          .put("oldStatus", odChangeStatusDTO.getOldStatus().toString());
    }
    if (odChangeStatusDTO.getNewStatus() != null) {
      sqlQuery += " AND oct.NEW_STATUS = :newStatus  ";
      parameters
          .put("newStatus", odChangeStatusDTO.getNewStatus().toString());
    }
    if (odChangeStatusDTO.getOdPriority() != null) {
      sqlQuery += " AND oct.OD_PRIORITY = :priority ";
      parameters.put("priority", odChangeStatusDTO.getOdPriority().toString());
    }
    if (odChangeStatusDTO.getIsDefault() != null) {
      sqlQuery += " AND oct.IS_DEFAULT = :is_default ";
      parameters.put("is_default", odChangeStatusDTO.getIsDefault().toString());
    }

    List<OdChangeStatusDTO> odChangeStatusDTOS = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(OdChangeStatusDTO.class));
    return odChangeStatusDTOS;
  }

  @Override
  public ResultInSideDto deleteCfg(Long odChangeStatusId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    OdChangeStatusEntity odChangeStatusEntity = getEntityManager()
        .find(OdChangeStatusEntity.class, odChangeStatusId);
    getEntityManager().remove(odChangeStatusEntity);
    resultInSideDTO.setId(odChangeStatusEntity.getId());
    resultInSideDTO.setObject(odChangeStatusEntity);
    resultInSideDTO.setKey(RESULT.SUCCESS);
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteListCfg(List<Long> listId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    for (Long id : listId) {
      resultInSideDTO = deleteCfg(id);
    }
    return resultInSideDTO;
  }

  @Override
  public String deleteLocaleList(List<Long> ids) {
    try {
      String sql = "DELETE FROM WFM.WO_TYPE_LOCALE WHERE WO_TYPE_ID in (:ids) ";
      Map idMaps = Collections.singletonMap("ids", ids);
      int status = getNamedParameterJdbcTemplate().update(sql, idMaps);
      if (status > 0) {
        return RESULT.SUCCESS;
      } else {
        return RESULT.ERROR;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.ERROR;
    }
  }

  public String delete(Long id) {
    try {
      OdChangeStatusEntity odChangeStatusEntity = getEntityManager()
          .find(OdChangeStatusEntity.class, id);
      getEntityManager().remove(odChangeStatusEntity);
      return RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return RESULT.ERROR;
  }

  @Override
  public int deleteList(List<Long> ids) {
    String sql = "DELETE from " + OdChangeStatusEntity.class.getSimpleName()
        + " t where 1 = 1 AND t.id in (:idx) ";
    Query query = getEntityManager().createQuery(sql);
    query.setParameter("idx", ids);
    return query.executeUpdate();
  }

  @Override
  public ResultInSideDto insertOrUpdate(OdChangeStatusDTO odChangeStatusDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    OdChangeStatusEntity oEntity = odChangeStatusDTO.toEntity();
    if (odChangeStatusDTO.getId() != null) {
      getEntityManager().merge(oEntity);
    } else {
      getEntityManager().persist(oEntity);
    }
    resultInSideDTO.setKey(RESULT.SUCCESS);
    resultInSideDTO.setId(oEntity.getId());
    return resultInSideDTO;
  }

  @Override
  public OdChangeStatusDTO findOdChangeStatusById(Long odChangeStatusId) {
    OdChangeStatusEntity dataEntity = getEntityManager()
        .find(OdChangeStatusEntity.class, odChangeStatusId);
    if (dataEntity != null) {
      return dataEntity.toDTO();
    }
    return null;
  }

  @Override
  public OdChangeStatusDTO findOdChangeStatusDTOById(Long odChangeStatusId) {

    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_OD_CHANGE_STATUS, "get-od-change-status-dto-byId");
    Map<String, String> param = new HashMap<>();
    param.put("id", odChangeStatusId.toString());
    List<OdChangeStatusDTO> odChangeStatusDTOS = getNamedParameterJdbcTemplate()
        .query(sqlQuery, param, BeanPropertyRowMapper.newInstance(OdChangeStatusDTO.class));

    if (odChangeStatusDTOS != null && odChangeStatusDTOS.size() > 0) {
      return odChangeStatusDTOS.get(0);
    }
    return null;
  }

  @Override
  public String getSeqOdChangeStatus() {
    String sqlQuery = "select OD_SEQ.nextval Id from dual";
    Map<String, String> params = new HashMap<>();
    List<OdChangeStatusDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, params, BeanPropertyRowMapper.newInstance(OdChangeStatusDTO.class));

    if (list != null && list.size() > 0) {
      return list.get(0).getId().toString();
    }
    return null;
  }

  @Override
  public List<OdTypeEntity> findAllOdType() {
    return findAll(OdTypeEntity.class);
  }

  @Override
  public List<OdExportCfgBusinessDTO> getOdCfgBusinessDataExport(
      OdChangeStatusDTO odChangeStatusDTO) {
    Datatable dataTable = getListOdCfgBusiness(odChangeStatusDTO);
    if (dataTable != null) {
      List<OdChangeStatusDTO> odChangeStatusDTOS = (List<OdChangeStatusDTO>) dataTable.getData();
      List<Long> arrId = new ArrayList<>();
      odChangeStatusDTOS.forEach(c -> {
        arrId.add(c.getId());
      });

      //same to screen ui, cause data export structure data search
      BaseDto baseOdCfgBusiness = sqlSearch(odChangeStatusDTO, "export-search");
      String sqlQuery = baseOdCfgBusiness.getSqlQuery();
      sqlQuery += " AND ocs.ID in (:ids) ORDER BY ocs.ID";
      baseOdCfgBusiness.setSqlQuery(sqlQuery);
      Map<String, Object> parameters = baseOdCfgBusiness.getParameters();
      parameters.put("ids", arrId);
      return getNamedParameterJdbcTemplate()
          .query(baseOdCfgBusiness.getSqlQuery(), baseOdCfgBusiness.getParameters(),
              BeanPropertyRowMapper.newInstance(OdExportCfgBusinessDTO.class));
    }
    return null;
  }

  @Override
  public boolean isExitOdChangeStatusDTO(OdChangeStatusDTO odChangeStatusDTO) {
    List<OdChangeStatusEntity> lst = null;
    boolean isExist = true;
    if (odChangeStatusDTO.getOdTypeId() != null) {
      lst = findByMultilParam(OdChangeStatusEntity.class, "odTypeId",
          odChangeStatusDTO.getOdTypeId(),
          "oldStatus", odChangeStatusDTO.getOldStatus(), "newStatus",
          odChangeStatusDTO.getNewStatus(),
          "odPriority", odChangeStatusDTO.getOdPriority());
    } else {
      lst = findByMultilParam(OdChangeStatusEntity.class,
          "oldStatus", odChangeStatusDTO.getOldStatus(), "newStatus",
          odChangeStatusDTO.getNewStatus(),
          "odPriority", odChangeStatusDTO.getOdPriority(), "isDefault", 1L);
    }
    if (lst == null || lst.size() < 1) {
      return false;
    } else {
      if (odChangeStatusDTO.getId() != null) {
        Long currentId = odChangeStatusDTO.getId();
        if (currentId.equals(lst.get(0).getId())) {
          return false;
        }
      }

      if (odChangeStatusDTO.getOdTypeId() == null) {
        for (OdChangeStatusEntity odChangeStatusEntity : lst) {
          if (odChangeStatusEntity.getOdTypeId() == null) {
            return true;
          }
        }
        isExist = false;
      }
    }
    return isExist;
  }

  @Override
  public OdChangeStatusDTO getOdChangeStatusDTOByParams(String... params) {
    try {
      List<OdChangeStatusEntity> lst = null;
      if (params != null) {
        if (params.length > 3) {
          lst = findByMultilParam(OdChangeStatusEntity.class,
              "oldStatus",
              StringUtils.isStringNullOrEmpty(params[0]) ? null : Long.valueOf(params[0]),
              "newStatus",
              StringUtils.isStringNullOrEmpty(params[1]) ? null : Long.valueOf(params[1]),
              "odPriority",
              StringUtils.isStringNullOrEmpty(params[2]) ? null : Long.valueOf(params[2]),
              "odTypeId",
              StringUtils.isStringNullOrEmpty(params[3]) ? null : Long.valueOf(params[3]));
        }
        if ((lst == null || lst.size() == 0) && params.length > 2) {
          lst = findByMultilParam(OdChangeStatusEntity.class,
              "oldStatus",
              StringUtils.isStringNullOrEmpty(params[0]) ? null : Long.valueOf(params[0])
              , "newStatus",
              StringUtils.isStringNullOrEmpty(params[1]) ? null : Long.valueOf(params[1]),
              "odPriority",
              StringUtils.isStringNullOrEmpty(params[2]) ? null : Long.valueOf(params[2]),
              "isDefault", 1L);
        }
      }
      if (lst != null && lst.size() > 0) {
        return lst.get(0).toDTO();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public String checkConstraint(List<Long> lstCondition) {
    try {
      String sqlCheckPriority = "SELECT 1 id FROM WFM.WO_PRIORITY WHERE WO_TYPE_ID in (:ids) ";
      String sqlCheckWoCdGroup = "SELECT 1 id FROM WFM.WO_CD_GROUP WHERE WO_TYPE_ID in (:ids) ";
      String sqlCheckWO = "SELECT 1 id FROM WFM.WO WHERE WO_TYPE_ID in (:ids) ";
      Map idMaps = Collections.singletonMap("ids", lstCondition);
      List<OdChangeStatusDTO> odChangeStatusDTOS = getNamedParameterJdbcTemplate()
          .query(sqlCheckPriority, idMaps,
              BeanPropertyRowMapper.newInstance(OdChangeStatusDTO.class));
      if (odChangeStatusDTOS != null && odChangeStatusDTOS.size() > 0) {
        return I18n.getLanguage("message.odChangeStatus.delete.fail.constraint.WO_PRIORITY");
      }
      odChangeStatusDTOS = getNamedParameterJdbcTemplate().query(sqlCheckWoCdGroup, idMaps,
          BeanPropertyRowMapper.newInstance(OdChangeStatusDTO.class));
      if (odChangeStatusDTOS != null && odChangeStatusDTOS.size() > 0) {
        return I18n.getLanguage("message.odChangeStatus.delete.fail.constraint.WO_CD_GROUP");
      }
      odChangeStatusDTOS = getNamedParameterJdbcTemplate()
          .query(sqlCheckWO, idMaps, BeanPropertyRowMapper.newInstance(OdChangeStatusDTO.class));
      if (odChangeStatusDTOS != null && odChangeStatusDTOS.size() > 0) {
        return I18n.getLanguage("message.odChangeStatus.delete.fail.constraint.WO");
      }
      return RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.ERROR + e.getMessage();
    }
  }

  public BaseDto sqlSearch(OdChangeStatusDTO odChangeStatusDTO, String queryId) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_OD_CHANGE_STATUS, queryId);
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("odStatusCode", OD_MASTER_CODE.OD_STATUS);
    parameters.put("odPriorityCode", OD_MASTER_CODE.OD_PRIORITY);
    if ("export-search".equals(queryId)) {
      parameters.put("odColumnCode", OD_MASTER_CODE.OD_CFG_BUSINESS_COLUMN);
    } else {
      if (StringUtils.isNotNullOrEmpty(odChangeStatusDTO.getSearchAll())) {
        sqlQuery += " AND (lower(ot.OD_TYPE_NAME) LIKE :searchAll ESCAPE '\\')  ";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(odChangeStatusDTO.getSearchAll()));
      }
      if (odChangeStatusDTO.getOdTypeId() != null) {
        sqlQuery += " AND ot.OD_TYPE_ID = :odTypeId ";
        parameters.put("odTypeId", odChangeStatusDTO.getOdTypeId().toString());
      }

      if (odChangeStatusDTO.getIsDefault() != null) {
        if (odChangeStatusDTO.getIsDefault() == 1) {
          sqlQuery += " AND ocs.IS_DEFAULT = 1 ";
        } else {
          sqlQuery += " AND NVL(ocs.IS_DEFAULT, 0) = 0 ";
        }
      }

      if (odChangeStatusDTO.getOldStatus() != null) {
        sqlQuery += " AND oldStatus.item_value = :oldStatus ";
        parameters
            .put("oldStatus", odChangeStatusDTO.getOldStatus().toString());
      }
      if (odChangeStatusDTO.getNewStatus() != null) {
        sqlQuery += " AND newStatus.item_value = :newStatus  ";
        parameters
            .put("newStatus", odChangeStatusDTO.getNewStatus().toString());
      }
      if (odChangeStatusDTO.getOdPriority() != null) {
        sqlQuery += " AND  od_pri.item_id = :priority ";
        parameters.put("priority", odChangeStatusDTO.getOdPriority().toString());
      }
    }

    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
