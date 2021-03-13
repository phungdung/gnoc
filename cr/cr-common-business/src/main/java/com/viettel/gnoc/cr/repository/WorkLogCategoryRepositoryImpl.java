package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.wo.dto.UserGroupCategoryDTO;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import com.viettel.gnoc.wo.model.WorkLogEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WorkLogCategoryRepositoryImpl extends BaseRepository implements
    WorkLogCategoryRepository {

  @Override
  public List<WorkLogCategoryInsideDTO> getListWorkLogCategoryDTO(
      WorkLogCategoryInsideDTO workLogCategoryDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MR_SERVICE,
            "get-List-Worklog-Category-By-WlayType");
    params.put("leeLocale",
        StringUtils.isStringNullOrEmpty(workLogCategoryDTO.getProxyLocale()) ? I18n.getLocale()
            : workLogCategoryDTO.getProxyLocale());
    if (!StringUtils.isStringNullOrEmpty(workLogCategoryDTO.getWlayType())) {
      sql += " AND lewlc.WLAY_TYPE = :wlayType ";
      params.put("wlayType", workLogCategoryDTO.getWlayType());
    }

    if (!StringUtils.isStringNullOrEmpty(workLogCategoryDTO.getWlayCode())) {
      sql += " AND lower(lewlc.WLAY_CODE) = :wlayCode ";
      params.put("wlayCode", workLogCategoryDTO.getWlayCode().toLowerCase());
    }

    List<WorkLogCategoryInsideDTO> list = getNamedParameterJdbcTemplate().query(sql, params,
        BeanPropertyRowMapper.newInstance(WorkLogCategoryInsideDTO.class));
    return list;
  }

  @Override
  public Datatable getListWorklogSearch(WorkLogInsiteDTO workLogInsiteDTO) {
    BaseDto baseDto = sqlGetListWorklogSearch(workLogInsiteDTO);
    Datatable lst = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        workLogInsiteDTO.getPage(), workLogInsiteDTO.getPageSize(), WorkLogInsiteDTO.class, null,
        null);
    return lst;
  }

  @Override
  public List<WorkLogInsiteDTO> getListWorkLogDTO(WorkLogInsiteDTO dto) {
    return onSearchEntity(WorkLogEntity.class, dto, dto.getPage(), dto.getPageSize(),
        dto.getSortType(), dto.getSortName());
  }

  @Override
  public List<UserGroupCategoryDTO> getListUserGroupBySystem(UserGroupCategoryDTO dto) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "get-List-User-Group-By-System");
    params.put("leeLocale", StringUtils.isStringNullOrEmpty(dto.getProxyLocale()) ? I18n.getLocale()
        : dto.getProxyLocale());
    if (!StringUtils.isStringNullOrEmpty(dto.getUgcySystem())) {
      sql += " AND leugc.ugcy_system = :ugcySystem ";
      params.put("ugcySystem", dto.getUgcySystem());
    }
    sql += " ORDER BY leugc.ugcy_code";
    List<UserGroupCategoryDTO> list = getNamedParameterJdbcTemplate().query(sql, params,
        BeanPropertyRowMapper.newInstance(UserGroupCategoryDTO.class));
    return list;
  }

  private BaseDto sqlGetListWorklogSearch(WorkLogInsiteDTO workLogInsiteDTO) {
    String locale = I18n.getLocale();
    String sql;
    Map<String, Object> params = new HashMap<>();
    if (locale != null && locale.toLowerCase().contains("en")) {
      sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "get-list-worklog-search-en");
    } else {
      sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "get-list-worklog-search");
    }
    //tim kiem theo ma don vi
    if (workLogInsiteDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(workLogInsiteDTO.getWlgObjectId())) {
        sql += " and a.wlg_object_id = :wlgObjectId ";
        params.put("wlgObjectId", workLogInsiteDTO.getWlgObjectId());
      }
      if (!StringUtils.isStringNullOrEmpty(workLogInsiteDTO.getWlgObjectType())) {
        sql += " and a.wlg_object_type = :wlgObjectType ";
        params.put("wlgObjectType", workLogInsiteDTO.getWlgObjectType());
      }
    }
    sql += " order by a.created_date desc ";
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(params);
    return baseDto;
  }
}
