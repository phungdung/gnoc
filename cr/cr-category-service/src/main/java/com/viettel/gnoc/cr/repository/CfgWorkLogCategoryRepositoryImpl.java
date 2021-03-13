package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.cr.model.WorkLogCategoryEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import viettel.passport.client.UserToken;

@Repository
@Slf4j
public class CfgWorkLogCategoryRepositoryImpl extends BaseRepository implements
    CfgWorkLogCategoryRepository {

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public WorkLogCategoryInsideDTO findWorkLogCategoryById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(WorkLogCategoryEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertWorkLogCategory(WorkLogCategoryInsideDTO workLogCategoryDTO) {
    WorkLogCategoryEntity entity = workLogCategoryDTO.toEntity();
    ResultInSideDto resultInSideDto = insertByModel(entity, colId);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("insertWorkLogCategory");
    logChangeConfigDTO.setContent("InsertWorkLogCategory with ID: " + resultInSideDto.getId());
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public String deleteWorkLogCategoryById(Long id) {
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("deleteWorkLogCategoryById");
    logChangeConfigDTO.setContent("DeleteWorkLogCategoryById with ID: " + id);
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return deleteById(WorkLogCategoryEntity.class, id, colId);
  }

  @Override
  public String updateWorkLogCategory(WorkLogCategoryInsideDTO dto) {
    WorkLogCategoryEntity entity = dto.toEntity();
    getEntityManager().merge(entity);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("updateWorkLogCategory");
    logChangeConfigDTO.setContent("UpdateWorkLogCategory with ID: " + dto.getWlayId());
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return RESULT.SUCCESS;
  }

  @Override
  public Datatable getListWorkLogCategory(WorkLogCategoryInsideDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_WORKLOG_CATEGORY, "getListWorkLogCategory");
    String locale = I18n.getLocale();

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", locale);
    if (StringUtils.isNotNullOrEmpty(dto.getWlayCode())) {
      sql += "  AND LOWER(list_result.wlayCode) LIKE :wlayCode ESCAPE '\\'";
      parameters.put("wlayCode",
          StringUtils.convertLowerParamContains(dto.getWlayCode()));
    }
    if (StringUtils.isNotNullOrEmpty(dto.getWlayNameType())) {
      sql += "  AND LOWER(list_result.wlayNameType) LIKE :wlayNameType ESCAPE '\\'";
      parameters.put("wlayNameType",
          StringUtils.convertLowerParamContains(dto.getWlayNameType()));
    }
    if (StringUtils.isNotNullOrEmpty(dto.getWlayName())) {
      sql += "  AND LOWER(list_result.wlayName) LIKE :wlayName ESCAPE '\\'";
      parameters.put("wlayName",
          StringUtils.convertLowerParamContains(dto.getWlayName()));
    }
    if (StringUtils.isNotNullOrEmpty(dto.getWlayDescription())) {
      sql += "  AND LOWER(list_result.wlayDescription) LIKE :wlayDescription ESCAPE '\\'";
      parameters.put("wlayDescription",
          StringUtils.convertLowerParamContains(dto.getWlayDescription()));
    }
    if (!StringUtils.isLongNullOrEmpty(dto.getWlayType())) {
      sql += "  AND list_result.wlayType = :wlayType ";
      parameters.put("wlayType",
          dto.getWlayType());
    }
    if (!StringUtils.isLongNullOrEmpty(dto.getWlayIsActive())) {
      sql += " AND list_result.wlayIsActive = :wlayIsActive ";
      parameters.put("wlayIsActive",
          dto.getWlayIsActive());
    }
    if (StringUtils.isNotNullOrEmpty(dto.getSearchAll())) {
      sql += " AND (LOWER(list_result.wlayCode) LIKE '%' || :searchAll || '%' ESCAPE '\\'";
      sql += " OR LOWER(list_result.wlayName) LIKE '%' || :searchAll || '%' ESCAPE '\\')";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(dto.getSearchAll()));
    }
    sql += " ORDER BY list_result.wlayId DESC";
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(),
        dto.getPageSize(), WorkLogCategoryInsideDTO.class,
        dto.getSortName(),
        dto.getSortType());
  }

  @Override
  public List<WorkLogCategoryInsideDTO> getListWorkLogType() {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_WORKLOG_CATEGORY, "getListWorkLogType");
    Map<String, Object> parameters = new HashMap<>();
    List<WorkLogCategoryInsideDTO> workLogCategoryDTOS = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper
            .newInstance(WorkLogCategoryInsideDTO.class));
    return workLogCategoryDTOS;
  }

  private static final String colId = "wlayId";
}
