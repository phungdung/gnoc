package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.MethodParameterDTO;
import com.viettel.gnoc.cr.dto.TempImportDTO;
import com.viettel.gnoc.cr.model.TempImportEntity;
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
public class CfgTempImportRepositoryImpl extends BaseRepository implements CfgTempImportRepository {

  @Autowired
  TicketProvider ticketProvider;

  private static final String colId = "tempImportId";

  @Override
  public Datatable getListTempImport(TempImportDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(), dto.getPage(),
        dto.getPageSize(), TempImportDTO.class,
        dto.getSortName(),
        dto.getSortType());
  }

  @Override
  public TempImportDTO findTempImportById(Long tempImportId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_TEMP_IMPORT, "getListTempImport");
    sqlQuery += " AND lr.TEMP_IMPORT_ID = :p_temp_import_id ";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    parameters.put("p_temp_import_id", tempImportId);
    Datatable data = getListDataTableBySqlQuery
        (sqlQuery, parameters, 1, 1, TempImportDTO.class, null, null);
    return (TempImportDTO) data.getData().get(0);
  }

  @Override
  public ResultInSideDto insertTempImport(TempImportDTO tempImportDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    TempImportEntity tempImportEntity = getEntityManager().merge(tempImportDTO.toEntity());
    resultInSideDTO.setId(tempImportEntity.getTempImportId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto updateTempImport(TempImportDTO tempImportDTO) {
    TempImportEntity entity = tempImportDTO.toEntity();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setId(tempImportDTO.getTempImportId());
    getEntityManager().merge(entity);
    return resultInSideDto;
  }

  @Override
  public String deleteTempImportById(Long id) {
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("deleteTempImportById with ID: " + id);
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return deleteById(TempImportEntity.class, id, colId);
  }

  @Override
  public List<MethodParameterDTO> getMethodPrameter() {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_TEMP_IMPORT, "getMethodPrameter");
    Map<String, Object> parameters = new HashMap<>();
    List<MethodParameterDTO> methodParameterDTOS = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper
            .newInstance(MethodParameterDTO.class));
    return methodParameterDTOS;
  }

  @Override
  public Datatable getListDataExport(TempImportDTO tempImportDTO) {
    BaseDto baseDto = sqlSearch(tempImportDTO);
    Datatable data = new Datatable();
    List<TempImportDTO> lstDTO = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(TempImportDTO.class));
    data.setData(lstDTO);
    return data;
  }

  @Override
  public List<TempImportDTO> getAllListTempImport(TempImportDTO tempImportDTO) {
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append("Select * from TEMP_IMPORT a where a.IS_ACTIVE = 1");

    List<TempImportDTO> list = getNamedParameterJdbcTemplate().query(
        sql.toString(), parameters, BeanPropertyRowMapper.newInstance(TempImportDTO.class)
    );

    return list;
  }

  @Override
  public TempImportDTO findTempImportDtoById(Long id) {
    TempImportEntity entity = getEntityManager()
        .find(TempImportEntity.class, id);
    if (entity != null) {
      return entity.toDTO();
    }
    return null;
  }

  public BaseDto sqlSearch(TempImportDTO tempImportDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_TEMP_IMPORT, "getListTempImport");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    if (StringUtils.isNotNullOrEmpty(tempImportDTO.getCode())) {
      sqlQuery += "  AND LOWER(lr.code) LIKE '%' || :code || '%' ESCAPE '\\'";
      parameters.put("code",
          StringUtils.convertLowerParamContains(tempImportDTO.getCode()));
    }
    if (StringUtils.isNotNullOrEmpty(tempImportDTO.getName())) {
      sqlQuery += "  AND LOWER(lr.name) LIKE '%' || :name || '%' ESCAPE '\\'";
      parameters.put("name",
          StringUtils.convertLowerParamContains(tempImportDTO.getName()));
    }
    if (StringUtils.isNotNullOrEmpty(tempImportDTO.getTitle())) {
      sqlQuery += "  AND LOWER(lr.title) LIKE '%' || :title || '%' ESCAPE '\\'";
      parameters.put("title",
          StringUtils.convertLowerParamContains(tempImportDTO.getTitle()));
    }
    if (tempImportDTO.getIsActive() != null) {
      sqlQuery += "  AND lr.IS_ACTIVE = :isActive";
      parameters.put("isActive", tempImportDTO.getIsActive());
    }
    if (StringUtils.isNotNullOrEmpty(tempImportDTO.getSearchAll())) {
      sqlQuery += " AND (LOWER(lr.code) LIKE '%' || :searchAll || '%' ESCAPE '\\'";
      sqlQuery += " OR LOWER(lr.name) LIKE '%' || :searchAll || '%' ESCAPE '\\')";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(tempImportDTO.getSearchAll()));
    }
    sqlQuery += " ORDER BY lr.CREATER_TIME DESC";

    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
