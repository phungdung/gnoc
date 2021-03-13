package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.model.CfgChildArrayEntity;
import com.viettel.gnoc.cr.model.ImpactSegmentEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author DungPV
 */
@Repository
@Slf4j
public class CfgChildArrayRepositoryImpl extends BaseRepository implements
    CfgChildArrayRepository {


  @Override
  public Datatable getListCfgChildArray(CfgChildArrayDTO cfgChildArrayDTO) {
    BaseDto baseDto = sqlSearch(cfgChildArrayDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        cfgChildArrayDTO.getPage(), cfgChildArrayDTO.getPageSize(),
        CfgChildArrayDTO.class,
        cfgChildArrayDTO.getSortName(), cfgChildArrayDTO.getSortType());
  }

  public List<ImpactSegmentDTO> getListImpactSegmentCBB() {
    List<ImpactSegmentEntity> lstEntity = findByMultilParam(ImpactSegmentEntity.class, "isActive",
        1L, "appliedSystem", 2L);
    List<ImpactSegmentDTO> lstDTO = new ArrayList<>();
    for (ImpactSegmentEntity entity : lstEntity) {
      lstDTO.add(entity.toDTO());
    }
    return lstDTO;
  }

  @Override
  public ResultInSideDto delete(Long childrenId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgChildArrayEntity cfgChildArrayEntity = getEntityManager()
        .find(CfgChildArrayEntity.class, childrenId);
    getEntityManager().remove(cfgChildArrayEntity);
    return resultInSideDto;
  }

  @Override
  public CfgChildArrayDTO getDetail(Long childrenId) {
    CfgChildArrayDTO cfgChildArrayDTO = new CfgChildArrayDTO();
    cfgChildArrayDTO.setChildrenId(childrenId);
    BaseDto baseDto = sqlSearch(cfgChildArrayDTO);
    List<CfgChildArrayDTO> lst = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(CfgChildArrayDTO.class));
    return lst.isEmpty() ? null : lst.get(0);
  }

  @Override
  public ResultInSideDto addOrUpdate(CfgChildArrayDTO cfgChildArrayDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgChildArrayEntity cfgChildArrayEntity = getEntityManager().merge(cfgChildArrayDTO.toEntity());
    resultInSideDto.setId(cfgChildArrayEntity.getChildrenId());
    return resultInSideDto;
  }

  @Override
  public List<CfgChildArrayDTO> getCbbChildArray(CfgChildArrayDTO dto) {
    List<CfgChildArrayDTO> lst = new ArrayList<>();
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_CHILD_ARRAY, "get-cbb-child-array");
      Map<String, Object> params = new HashMap<>();
      params.put("p_leeLocale", I18n.getLocale());
      //namtn edit on November 06
      if (dto != null) {
        if (dto.getParentId() != null) {
          sql += " AND cr.PARENT_ID = :parent_id ";
          params.put("parent_id", dto.getParentId());
        }
        if (dto.getStatus() != null) {
          sql += " AND cr.STATUS = :status ";
          params.put("status", dto.getStatus());
        }
      }

      lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CfgChildArrayDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public BaseDto sqlSearch(CfgChildArrayDTO cfgChildArrayDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery;
    sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_CHILD_ARRAY, "get-all-cfg-child-array");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    if (cfgChildArrayDTO != null) {
      if (StringUtils.isNotNullOrEmpty(cfgChildArrayDTO.getSearchAll())) {
        sqlQuery += " AND (lower(cr.CHILDREN_CODE) LIKE :searchAll ESCAPE '\\' OR lower(cr.CHILDREN_NAME) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(cfgChildArrayDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(cfgChildArrayDTO.getParentId())) {
        sqlQuery += " AND cr.PARENT_ID= :parentId ";
        parameters.put("parentId", cfgChildArrayDTO.getParentId());
      }
      if (!StringUtils.isStringNullOrEmpty(cfgChildArrayDTO.getParentCode())) {
        sqlQuery += " AND lower(cr.IMPACT_SEGMENT_CODE) LIKE :parentCode ESCAPE '\\' ";
        parameters.put("parentCode",
            StringUtils.convertLowerParamContains(cfgChildArrayDTO.getParentCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(cfgChildArrayDTO.getParentName())) {
        sqlQuery += " AND lower(cr.IMPACT_SEGMENT_NAME) LIKE :parentName ESCAPE '\\' ";
        parameters.put("parentName",
            StringUtils.convertLowerParamContains(cfgChildArrayDTO.getParentName()));
      }
      if (!StringUtils.isStringNullOrEmpty(cfgChildArrayDTO.getChildrenCode())) {
        sqlQuery += " AND lower(cr.CHILDREN_CODE) LIKE :childrenCode ESCAPE '\\' ";
        parameters
            .put("childrenCode",
                StringUtils.convertLowerParamContains(cfgChildArrayDTO.getChildrenCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(cfgChildArrayDTO.getChildrenName())) {
        sqlQuery += " AND (lower(cr.CHILDREN_NAME) LIKE :childrenName ESCAPE '\\' )";
        parameters
            .put("childrenName",
                StringUtils.convertLowerParamContains(cfgChildArrayDTO.getChildrenName()));
      }
      if (!StringUtils.isStringNullOrEmpty(cfgChildArrayDTO.getStatus())) {
        sqlQuery += " AND cr.STATUS= :status ";
        parameters.put("status", cfgChildArrayDTO.getStatus());
      }
      if (!StringUtils.isStringNullOrEmpty(cfgChildArrayDTO.getUpdatedUser())) {
        sqlQuery += " AND (lower(cr.UPDATED_USER) LIKE :updatedUser ESCAPE '\\' )";
        parameters
            .put("updatedUser",
                StringUtils.convertLowerParamContains(cfgChildArrayDTO.getUpdatedUser()));
      }
      if (StringUtils.isNotNullOrEmpty(cfgChildArrayDTO.getCreateTimeFrom()) && StringUtils
          .isNotNullOrEmpty(cfgChildArrayDTO.getCreateTimeTo())) {
        sqlQuery +=
            " and to_char(cr.UPDATED_TIME,'dd/MM/yyyy HH24:mi:ss') BETWEEN '" + cfgChildArrayDTO
                .getCreateTimeFrom() + "' AND '" + cfgChildArrayDTO.getCreateTimeTo() + "' ";
      }
      if (cfgChildArrayDTO.getChildrenId() != null && cfgChildArrayDTO.getChildrenId() > 0) {
        sqlQuery += " AND cr.CHILDREN_ID =:CHILDREN_ID ";
        parameters.put("CHILDREN_ID", cfgChildArrayDTO.getChildrenId());
      }

    }
    sqlQuery += " order by cr.UPDATED_TIME DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
