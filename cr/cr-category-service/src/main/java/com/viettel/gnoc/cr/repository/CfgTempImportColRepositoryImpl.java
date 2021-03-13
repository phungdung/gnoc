package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.TempImportColDTO;
import com.viettel.gnoc.cr.model.TempImportColEntity;
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
public class CfgTempImportColRepositoryImpl extends BaseRepository implements
    CfgTempImportColRepository {

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public TempImportColDTO findTempImportColById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(TempImportColEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public List<TempImportColDTO> findTempImportColByTempImportId(Long tempImportId) {
    if (tempImportId != null && tempImportId > 0) {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_TEMP_IMPORT_COL,
              "findTempImportColByTempImportId");
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("tempImportId", tempImportId);
      return getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(TempImportColDTO.class));
    }
    return null;
  }

  @Override
  public ResultInSideDto insertTempImportCol(TempImportColDTO tempImportColDTO) {
    TempImportColEntity entity = tempImportColDTO.toEntity();
    ResultInSideDto resultInSideDto = insertByModel(entity, colId);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("insertTempImportCol");
    logChangeConfigDTO.setContent("InsertTempImportCol with ID: " + resultInSideDto.getId());
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public String deleteTempImportColById(Long id) {
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("deleteTempImportColById");
    logChangeConfigDTO.setContent("DeleteTempImportColById with ID: " + id);
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return deleteById(TempImportColEntity.class, id, colId);
  }

  @Override
  public String updateTempImportCol(TempImportColDTO dto) {
    TempImportColEntity entity = dto.toEntity();
    getEntityManager().merge(entity);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("updateTempImportCol");
    logChangeConfigDTO.setContent("UpdateTempImportCol with ID: " + dto.getTempImportColId());
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return RESULT.SUCCESS;
  }

  @Override
  public Datatable getListTempImportCol(TempImportColDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_TEMP_IMPORT_COL, "getListTempImportCol");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(dto.getCode())) {
      sql += "  AND LOWER(t1.CODE) LIKE '%' || :code || '%' ESCAPE '\\'";
      parameters.put("code",
          StringUtils.convertLowerParamContains(dto.getCode()));
    }
    if (StringUtils.isNotNullOrEmpty(dto.getTitle())) {
      sql += "  AND LOWER(t1.TITLE) LIKE '%' || :title || '%' ESCAPE '\\'";
      parameters.put("title",
          StringUtils.convertLowerParamContains(dto.getTitle()));
    }
    if (StringUtils.isNotNullOrEmpty(dto.getSearchAll())) {
      sql += " AND (LOWER(t1.CODE) LIKE '%' || :searchAll || '%' ESCAPE '\\'";
      sql += " OR LOWER(t1.TITLE) LIKE '%' || :searchAll || '%' ESCAPE '\\')";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(dto.getSearchAll()));
    }
    sql += " ORDER BY t1.TEMP_IMPORT_COL_ID DESC";
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(),
        dto.getPageSize(), TempImportColDTO.class,
        dto.getSortName(),
        dto.getSortType());
  }

  @Override
  public String deleteListTempImportColByTempImportId(List<TempImportColDTO> tempImportColDTOS) {
    return deleteByListDTO(tempImportColDTOS, TempImportColEntity.class, colTempImportId);
  }

  private static final String colId = "tempImportColId";
  private static final String colTempImportId = "tempImportId";
}
