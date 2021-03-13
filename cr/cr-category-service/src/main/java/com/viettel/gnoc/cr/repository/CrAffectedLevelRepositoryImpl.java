package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrAffectedLevelDTO;
import com.viettel.gnoc.cr.model.CrAffectedLevelEntity;
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
public class CrAffectedLevelRepositoryImpl extends BaseRepository implements
    CrAffectedLevelRepository {


  @Override
  public BaseDto sqlSearch(CrAffectedLevelDTO crAffectedLevelDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_Affected_Level, "CrAffectedLevel-list");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    if (StringUtils.isNotNullOrEmpty(crAffectedLevelDTO.getSearchAll())) {
      StringBuilder sqlSearchAll = new StringBuilder(
          " AND (lower(affected_level_code) LIKE :searchAll ESCAPE '\\' ");
      sqlSearchAll.append(" OR lower(affected_level_name) LIKE :searchAll ESCAPE '\\' ) ");
      sqlQuery += sqlSearchAll.toString();
      parameters.put("searchAll", StringUtils.convertLowerParamContains(
          crAffectedLevelDTO.getSearchAll()));
    }
    if (StringUtils.isNotNullOrEmpty(crAffectedLevelDTO.getAffectedLevelCode())) {
      sqlQuery += " AND LOWER(affected_level_code) LIKE :affected_level_code ESCAPE '\\' ";
      parameters.put("affected_level_code", StringUtils.convertLowerParamContains
          (crAffectedLevelDTO.getAffectedLevelCode()));
    }
    if (StringUtils.isNotNullOrEmpty(crAffectedLevelDTO.getAffectedLevelName())) {
      sqlQuery += " AND LOWER(affected_level_name) LIKE :affected_level_name ESCAPE '\\' ";
      parameters.put("affected_level_name", StringUtils.convertLowerParamContains
          (crAffectedLevelDTO.getAffectedLevelName()));
    }
    if (crAffectedLevelDTO.getTwoApproveLevel() != null) {
      sqlQuery += " AND two_approve_level =:two_approve_level ";
      parameters.put("two_approve_level", crAffectedLevelDTO.getTwoApproveLevel());
    }
    if (crAffectedLevelDTO.getAppliedSystem() != null) {
      sqlQuery += " AND applied_system  =:applied_system ";
      parameters.put("applied_system", crAffectedLevelDTO.getAppliedSystem());
    }

//    sqlQuery += " order by  affected_level_code asc ";
    sqlQuery += " order by  affected_level_id DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public Datatable getListCrAffectedLevel(CrAffectedLevelDTO crAffectedLevelDTO) {
    BaseDto baseDto = sqlSearch(crAffectedLevelDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(), crAffectedLevelDTO.getPage(),
        crAffectedLevelDTO.getPageSize(), CrAffectedLevelDTO.class,
        crAffectedLevelDTO.getSortName(), crAffectedLevelDTO.getSortType());
    datatable.setData(getList((List<CrAffectedLevelDTO>) datatable.getData()));
    return datatable;
  }

  @Override
  public ResultInSideDto addOrUpdateCrAffectedLevel(CrAffectedLevelDTO crAffectedLevelDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    CrAffectedLevelEntity entity = getEntityManager().merge(crAffectedLevelDTO.toEntity());
    resultInSideDto.setId(entity.getAffectedLevelId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteCrAffectedLevel(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    CrAffectedLevelEntity entity = getEntityManager().find(CrAffectedLevelEntity.class, id);
    getEntityManager().remove(entity);
    return resultInSideDto;
  }

  @Override
  public CrAffectedLevelDTO getDetail(Long crId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_Affected_Level, "CrAffectedLevel-list");
    sqlQuery += " AND affected_level_id =:affected_level_id ";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    parameters.put("affected_level_id", crId);
    List<CrAffectedLevelDTO> data = getNamedParameterJdbcTemplate().query
        (sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CrAffectedLevelDTO.class));
    return data.isEmpty() ? null : data.get(0);
  }

  @Override
  public List<CrAffectedLevelDTO> getListDataExport(CrAffectedLevelDTO crAffectedLevelDTO) {
    BaseDto baseDto = sqlSearch(crAffectedLevelDTO);
    List<CrAffectedLevelDTO> lstEx = getNamedParameterJdbcTemplate().query(
        baseDto.getSqlQuery(), baseDto.getParameters(), BeanPropertyRowMapper.newInstance(
            CrAffectedLevelDTO.class));
    return getList(lstEx);
  }

  private List<CrAffectedLevelDTO> getList(List<CrAffectedLevelDTO> lstCr) {
    List<CrAffectedLevelDTO> lst = new ArrayList<>();
    for (CrAffectedLevelDTO dto : lstCr) {
      dto.setAffectedLevelCode(dto.getAffectedLevelCode());
      dto.setAffectedLevelName(dto.getAffectedLevelName());
      if (dto.getAppliedSystem() != null) {
        if (dto.getAppliedSystem() == 1) {
          dto.setAppliedSystemName(I18n.getLanguage("cr.affectedLevel"));
        } else {
          dto.setAppliedSystemName(I18n.getLanguage("mr.affectedLevel"));
        }
      }
      if (dto.getTwoApproveLevel() != null) {
        if (dto.getTwoApproveLevel() == 1) {
          dto.setTwoApproveLevelName(I18n.getLanguage("common.yes"));
        } else {
          dto.setTwoApproveLevelName(I18n.getLanguage("common.no"));
        }
      }
      lst.add(dto);

    }
    return lst;
  }
}
