package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.OD_MASTER_CODE;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.od.dto.OdCfgBusinessDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusDTO;
import com.viettel.gnoc.od.model.OdCfgBusinessEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author TienNV
 */
@Repository
@Slf4j
public class OdCfgBusinessRepositoryImpl extends BaseRepository implements OdCfgBusinessRepository {

  @Override
  public ResultInSideDto deleteByOdChangeStatusId(Long odChangeStatusId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    List<OdCfgBusinessEntity> datas = findByMultilParam(OdCfgBusinessEntity.class,
        "odChangeStatusId", odChangeStatusId);
    for (OdCfgBusinessEntity data : datas) {
      getEntityManager().remove(data);
    }
    resultInSideDTO.setKey(RESULT.SUCCESS);
    return resultInSideDTO;
  }

  @Override
  public String delete(Long id) {
    try {
      OdCfgBusinessEntity odCfgBusinessEntity = getEntityManager()
          .find(OdCfgBusinessEntity.class, id);
      getEntityManager().remove(odCfgBusinessEntity);
      return RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return RESULT.ERROR;
  }

  @Override
  public ResultInSideDto add(OdChangeStatusDTO odChangeStatusDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    List<OdCfgBusinessDTO> odCfgBusinessDTOs = odChangeStatusDTO.getOdCfgBusinessDTO();
    odCfgBusinessDTOs.forEach(c -> {
      c.setId(null);
      c.setOdChangeStatusId(odChangeStatusDTO.getId());
      getEntityManager().persist(c.toEntity());
    });
    resultInSideDTO.setKey(RESULT.SUCCESS);
    return resultInSideDTO;
  }

  @Override
  public Long insertOrUpdate(OdCfgBusinessDTO odCfgBusinessDTO) {
    if (odCfgBusinessDTO.getId() != null && odCfgBusinessDTO.getId() > 0) {
      getEntityManager().merge(odCfgBusinessDTO.toEntity());
      return odCfgBusinessDTO.getId();
    } else {
      OdCfgBusinessEntity entity = odCfgBusinessDTO.toEntity();
      getEntityManager().persist(entity);
      return entity.getId();
    }

  }

  @Override
  public List<String> getListSequense(String seq, int size) {
    List<String> lstSequense = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      lstSequense.add(getSeqTableBase(seq));
    }
    return lstSequense;
  }

  @Override
  public OdCfgBusinessDTO findOdCfgBusinessById(Long odCfgBusinessId) {
    OdCfgBusinessEntity dataEntity = getEntityManager()
        .find(OdCfgBusinessEntity.class, odCfgBusinessId);
    if (dataEntity != null) {
      return dataEntity.toDTO();
    }
    return null;
  }

  @Override
  public List<OdCfgBusinessDTO> getListOdCfgBusiness(OdChangeStatusDTO odChangeStatusDTO) {
    BaseDto baseOdCfgBusiness = sqlSearchByOdChange(odChangeStatusDTO);
    List<OdCfgBusinessDTO> list = getNamedParameterJdbcTemplate()
        .query(baseOdCfgBusiness.getSqlQuery(), baseOdCfgBusiness.getParameters(),
            BeanPropertyRowMapper.newInstance(OdCfgBusinessDTO.class));
    return list;
  }

  @Override
  public int deleteList(List<Long> ids) {
    String sql = "DELETE from " + OdCfgBusinessEntity.class.getSimpleName()
        + " t where 1 = 1 AND t.id in (:idx) ";
    Query query = getEntityManager().createQuery(sql);
    query.setParameter("idx", ids);
    return query.executeUpdate();
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

  @Override
  public String checkConstraint(List<Long> lstCondition) {
    try {
      String sqlCheckPriority = "SELECT 1 id FROM WFM.WO_PRIORITY WHERE WO_TYPE_ID in (:ids) ";
      String sqlCheckWoCdGroup = "SELECT 1 id FROM WFM.WO_CD_GROUP WHERE WO_TYPE_ID in (:ids) ";
      String sqlCheckWO = "SELECT 1 id FROM WFM.WO WHERE WO_TYPE_ID in (:ids) ";
      Map idMaps = Collections.singletonMap("ids", lstCondition);
      List<OdCfgBusinessDTO> odCfgBusinessDTOS = getNamedParameterJdbcTemplate()
          .query(sqlCheckPriority, idMaps,
              BeanPropertyRowMapper.newInstance(OdCfgBusinessDTO.class));
      if (odCfgBusinessDTOS != null && odCfgBusinessDTOS.size() > 0) {
        return I18n.getMessages("message.odCfgBusiness.delete.fail.constraint.WO_PRIORITY");
      }
      odCfgBusinessDTOS = getNamedParameterJdbcTemplate().query(sqlCheckWoCdGroup, idMaps,
          BeanPropertyRowMapper.newInstance(OdCfgBusinessDTO.class));
      if (odCfgBusinessDTOS != null && odCfgBusinessDTOS.size() > 0) {
        return I18n.getMessages("message.odCfgBusiness.delete.fail.constraint.WO_CD_GROUP");
      }
      odCfgBusinessDTOS = getNamedParameterJdbcTemplate()
          .query(sqlCheckWO, idMaps, BeanPropertyRowMapper.newInstance(OdCfgBusinessDTO.class));
      if (odCfgBusinessDTOS != null && odCfgBusinessDTOS.size() > 0) {
        return I18n.getMessages("message.odCfgBusiness.delete.fail.constraint.WO");
      }
      return RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.ERROR + e.getMessage();
    }
  }

  public BaseDto sqlSearchByOdChange(OdChangeStatusDTO odChangeStatusDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_OD_CFG_BUSINESS, "get-detail");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("changeStatusId", odChangeStatusDTO.getId().toString());
    parameters.put("cfgBusinessColumn", OD_MASTER_CODE.OD_CFG_BUSINESS_COLUMN);
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

//  public BaseDto sqlSearch(OdChangeStatusDTO odChangeStatusDTO, String queryId) {
//    BaseDto baseDto = new BaseDto();
//    String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_OD_CFG_BUSINESS, queryId);
//    Map<String, Object> parameters = new HashMap<>();
//    parameters.put("odStatusCode", OD_MASTER_CODE.OD_STATUS);
//    parameters.put("odPriorityCode", OD_MASTER_CODE.OD_PRIORITY);
//    if(queryId.equals("export-search")) {
//      parameters.put("odColumnCode", OD_MASTER_CODE.OD_CFG_BUSINESS_COLUMN);
//    } else {
//      if (odChangeStatusDTO.getOdTypeId() != null) {
//        sqlQuery += " AND ot.OD_TYPE_ID = :odTypeId ";
//        parameters.put("odTypeId", odChangeStatusDTO.getOdTypeId().toString());
//      }
//
//      if (odChangeStatusDTO.getOldStatus() != null) {
//        sqlQuery += " AND oldStatus.item_value = :oldStatus ";
//        parameters
//            .put("oldStatus", odChangeStatusDTO.getOldStatus().toString());
//      }
//      if (odChangeStatusDTO.getNewStatus() != null) {
//        sqlQuery += " AND oldStatus.item_value = :newStatus  ";
//        parameters
//            .put("newStatus", odChangeStatusDTO.getNewStatus().toString());
//      }
//      if (odChangeStatusDTO.getOdPriority() != null) {
//        sqlQuery += " AND  od_pri.item_id = :priority ";
//        parameters.put("priority", odChangeStatusDTO.getOdPriority().toString());
//      }
//    }
//
//    baseDto.setSqlQuery(sqlQuery);
//    baseDto.setParameters(parameters);
//    return baseDto;
//  }

}
