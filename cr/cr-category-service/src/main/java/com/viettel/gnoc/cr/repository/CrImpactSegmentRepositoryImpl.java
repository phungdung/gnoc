package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.model.ImpactSegmentEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DungPV
 */
@Repository
@Slf4j
@Transactional
public class CrImpactSegmentRepositoryImpl extends BaseRepository implements
    CrImpactSegmentRepository {

  @Override
  public BaseDto sqlSearch(ImpactSegmentDTO impactSegmentDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = "";
    sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_Impact_Segment, "ImpactSegment-List");
    Map<String, Object> parameter = new HashMap<>();
    parameter.put("p_leeLocale", I18n.getLocale());
    if (StringUtils.isNotNullOrEmpty(impactSegmentDTO.getSearchAll())) {
      StringBuilder sqlSearchAll = new StringBuilder(
          " AND ( (LOWER(IMPACT_SEGMENT_CODE)) LIKE:searchAll ESCAPE '\\' ");
      sqlSearchAll.append(" OR (LOWER(IMPACT_SEGMENT_NAME)) LIKE:searchAll ESCAPE '\\' ) ");
      sqlQuery += sqlSearchAll.toString();
      parameter.put("searchAll", StringUtils.convertLowerParamContains(
          impactSegmentDTO.getSearchAll()));
    }

    if (StringUtils.isNotNullOrEmpty(impactSegmentDTO.getImpactSegmentCode())) {
      sqlQuery += " AND (LOWER(IMPACT_SEGMENT_CODE)) LIKE:IMPACT_SEGMENT_CODE ESCAPE '\\' ";
      parameter.put("IMPACT_SEGMENT_CODE", StringUtils.convertLowerParamContains(
          impactSegmentDTO.getImpactSegmentCode()));
    }
    if (StringUtils.isNotNullOrEmpty(impactSegmentDTO.getImpactSegmentName())) {
      sqlQuery += " AND (LOWER(IMPACT_SEGMENT_NAME)) LIKE:IMPACT_SEGMENT_NAME ESCAPE '\\' ";
      parameter.put("IMPACT_SEGMENT_NAME", StringUtils.convertLowerParamContains(
          impactSegmentDTO.getImpactSegmentName()));
    }
    if (impactSegmentDTO.getAppliedSystem() != null && impactSegmentDTO.getAppliedSystem() > -1) {
      sqlQuery += " AND APPLIED_SYSTEM=:APPLIED_SYSTEM ";
      parameter.put("APPLIED_SYSTEM", impactSegmentDTO.getAppliedSystem());
    }

//    sqlQuery += " order by  impact_segment_code asc ";
    sqlQuery += " order by  IMPACT_SEGMENT_ID DESC ";
    baseDto.setPage(impactSegmentDTO.getPage());
    baseDto.setPageSize(impactSegmentDTO.getPageSize());
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameter);
    return baseDto;
  }

  @Override
  public Datatable getListImpactSegment(ImpactSegmentDTO impactSegmentDTO) {
    BaseDto baseDto = sqlSearch(impactSegmentDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        baseDto.getPage(), baseDto.getPageSize(), ImpactSegmentDTO.class,
        impactSegmentDTO.getSortName(),
        impactSegmentDTO.getSortType());
    datatable.setData(getList((List<ImpactSegmentDTO>) datatable.getData()));
    return datatable;
  }

  @Override
  public ResultInSideDto addOrEditImpactSegment(ImpactSegmentDTO impactSegmentDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    ImpactSegmentEntity addOrUpdate = getEntityManager().merge(impactSegmentDTO.toEntity());
    resultInSideDto.setId(addOrUpdate.getImpactSegmentId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteImpactSegment(Long crImpactSegmentId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    ImpactSegmentEntity entity = getEntityManager()
        .find(ImpactSegmentEntity.class, crImpactSegmentId);
    getEntityManager().remove(entity);

    return resultInSideDto;
  }

  @Override
  public List<ImpactSegmentDTO> getListDataExport(ImpactSegmentDTO impactSegmentDTO) {
    BaseDto baseDto = sqlSearch(impactSegmentDTO);
    List<ImpactSegmentDTO> lstEx = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(ImpactSegmentDTO.class));
    return getList(lstEx);
  }

  private List<ImpactSegmentDTO> getList(List<ImpactSegmentDTO> lstCr) {
    List<ImpactSegmentDTO> lst = new ArrayList<>();
    for (ImpactSegmentDTO dto : lstCr) {
      if (dto.getAppliedSystem() != null) {
        if (dto.getAppliedSystem() == 1) {
          dto.setAppliedSystemName(I18n.getLanguage("mr.affectedLevel"));
        } else {
          dto.setAppliedSystemName(I18n.getLanguage("cr.affectedLevel"));
        }
      }
      lst.add(dto);
    }
    return lst;
  }

  @Override
  public ImpactSegmentDTO getDetail(Long crId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_Impact_Segment, "ImpactSegment-List");
    Map<String, Object> parameters = new HashMap<>();
    sqlQuery += " AND IMPACT_SEGMENT_ID=:Id ";
    parameters.put("p_leeLocale", I18n.getLocale());
    parameters.put("Id", crId);
    List<ImpactSegmentDTO> lstDTO = getNamedParameterJdbcTemplate().query(sqlQuery, parameters,
        BeanPropertyRowMapper.newInstance(ImpactSegmentDTO.class));
    return lstDTO.get(0);
  }

  @Override
  public List<ImpactSegmentDTO> getListImpactSegmentDTO(ImpactSegmentDTO impactSegmentDTO) {
    List<ImpactSegmentEntity> lst = findByMultilParam(ImpactSegmentEntity.class,
        "isActive", impactSegmentDTO.getIsActive(), "appliedSystem",
        impactSegmentDTO.getAppliedSystem());
    List<ImpactSegmentDTO> lstDTO = new ArrayList<>();
    for (ImpactSegmentEntity entity : lst) {
      lstDTO.add(entity.toDTO());
    }
    return lstDTO;
  }

  @Override
  public ImpactSegmentDTO findImpactSegmentBy(ImpactSegmentDTO dto) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append("select * from IMPACT_SEGMENT a where 1=1 ");

    if (!StringUtils.isStringNullOrEmpty(dto.getImpactSegmentCode())) {
      sql.append(" and a.IMPACT_SEGMENT_CODE like  :impact_segment_code and APPLIED_SYSTEM = 2");
      parameters.put("impact_segment_code", dto.getImpactSegmentCode());
    }

    List<ImpactSegmentDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(ImpactSegmentDTO.class));

    if (list != null && !list.isEmpty()) {
      return list.get(0);
    }

    return null;
  }
}
