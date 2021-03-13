package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.cr.model.WorkLogCategoryEntity;
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
        .getSqlQueryById(SQLBuilder.SQL_MR_MAINTENANCE_MNGT, "get-List-Worklog-Category-By-WlayType");
    params.put("leeLocale", StringUtils.isStringNullOrEmpty(workLogCategoryDTO.getProxyLocale()) ? I18n.getLocale()
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
}
