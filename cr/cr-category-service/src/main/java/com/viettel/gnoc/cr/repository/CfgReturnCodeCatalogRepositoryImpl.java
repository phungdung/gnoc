package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ReturnCodeCatalogDTO;
import com.viettel.gnoc.cr.model.ReturnCodeCatalogEntity;
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
public class CfgReturnCodeCatalogRepositoryImpl extends BaseRepository implements
    CfgReturnCodeCatalogRepository {

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public ReturnCodeCatalogDTO findCfgReturnCodeCatalogById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(ReturnCodeCatalogEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertCfgReturnCodeCatalog(ReturnCodeCatalogDTO returnCodeCatalogDTO) {
    ReturnCodeCatalogEntity entity = returnCodeCatalogDTO.toEntity();
    ResultInSideDto resultInSideDto = insertByModel(entity, colId);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("InsertCfgReturnCodeCatalog");
    logChangeConfigDTO.setContent("InsertCfgReturnCodeCatalog with ID: " + resultInSideDto.getId());
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public String deleteCfgReturnCodeCatalogById(Long id) {
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("DeleteCfgReturnCodeCatalogById");
    logChangeConfigDTO.setContent("DeleteCfgReturnCodeCatalogById with ID: " + id);
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return deleteById(ReturnCodeCatalogEntity.class, id, colId);
  }

  @Override
  public String updateCfgReturnCodeCatalog(ReturnCodeCatalogDTO dto) {
    ReturnCodeCatalogEntity entity = dto.toEntity();
    getEntityManager().merge(entity);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("UpdateCfgReturnCodeCatalog");
    logChangeConfigDTO
        .setContent("UpdateCfgReturnCodeCatalog with ID: " + dto.getReturnCodeCategoryId());
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return RESULT.SUCCESS;
  }

  @Override
  public Datatable getListReturnCodeCatalog(ReturnCodeCatalogDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_RETURN_CODE_CATALOG, "getListReturnCodeCatalog");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(dto.getReturnCode())) {
      sql += "  AND LOWER(t3.returnCode) LIKE '%' || :returnCode || '%' ESCAPE '\\'";
      parameters.put("returnCode",
          StringUtils.convertLowerParamContains(dto.getReturnCode()));
    }
    if (StringUtils.isNotNullOrEmpty(dto.getReturnTitle())) {
      sql += "  AND LOWER(t3.returnTitle) LIKE '%' || :returnTitle || '%' ESCAPE '\\'";
      parameters.put("returnTitle",
          StringUtils.convertLowerParamContains(dto.getReturnTitle()));
    }
    if (StringUtils.isNotNullOrEmpty(dto.getDescription())) {
      sql += "  AND LOWER(t3.description) LIKE '%' || :description || '%' ESCAPE '\\'";
      parameters.put("description",
          StringUtils.convertLowerParamContains(dto.getDescription()));
    }
    if (!StringUtils.isLongNullOrEmpty(dto.getReturnCategory())) {
      sql += " AND t3.returnCategory = :returnCategory ";
      parameters.put("returnCategory",
          dto.getReturnCategory());
    }
    if (!StringUtils.isLongNullOrEmpty(dto.getIsActive())) {
      sql += " AND t3.isActive = :isActive ";
      parameters.put("isActive",
          dto.getIsActive());
    }
    if (StringUtils.isNotNullOrEmpty(dto.getSearchAll())) {
      sql += " AND (LOWER(t3.returnCode) LIKE '%' || :searchAll || '%' ESCAPE '\\')";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(dto.getSearchAll()));
    }
    sql += " ORDER BY t3.returnCodeCategoryId DESC";
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(),
        dto.getPageSize(), ReturnCodeCatalogDTO.class,
        dto.getSortName(),
        dto.getSortType());
  }

  @Override
  public List<ReturnCodeCatalogDTO> getListReturnCategory() {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_RETURN_CODE_CATALOG, "getListReturnCategory");
    Map<String, Object> parameters = new HashMap<>();
    List<ReturnCodeCatalogDTO> codeCatalogDTOS = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper
            .newInstance(ReturnCodeCatalogDTO.class));
    return codeCatalogDTOS;
  }

  private static final String colId = "returnCodeCategoryId";
}
