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
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import com.viettel.gnoc.cr.model.CrImpactFrameEntity;
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
public class CfgCrImpactFrameRepositoryImpl extends BaseRepository implements
    CfgCrImpactFrameRepository {

  private static final String colId = "impactFrameId";

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public CrImpactFrameInsiteDTO findCrImpactFrameById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(CrImpactFrameEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertCrImpactFrame(CrImpactFrameInsiteDTO crImpactFrameDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (checkExisted(crImpactFrameDTO.getStartTime(), crImpactFrameDTO.getEndTime(), null)) {
      resultInSideDto.setKey(RESULT.DUPLICATE);
      resultInSideDto.setMessage(I18n.getLanguage("crImpactFrame.unique"));
      return resultInSideDto;
    }
    CrImpactFrameEntity entity = crImpactFrameDTO.toEntity();
    resultInSideDto = insertByModel(entity, colId);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
      logChangeConfigDTO.setUpdateTime(new Date());
      logChangeConfigDTO.setUserChange(userToken.getUserName());
      logChangeConfigDTO.setFunctionCode("insertCrImpactFrame");
      logChangeConfigDTO.setContent("InsertCrImpactFrame with ID: " + resultInSideDto.getId());
      getEntityManager().merge(logChangeConfigDTO.toEntity());
    }
    return resultInSideDto;
  }

  @Override
  public String deleteCrImpactFrameById(Long id) {
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("deleteCrImpactFrameById");
    logChangeConfigDTO.setContent("DeleteCrImpactFrameById with ID: " + id);
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return deleteById(CrImpactFrameEntity.class, id, colId);
  }

  @Override
  public ResultInSideDto updateCrImpactFrame(CrImpactFrameInsiteDTO dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (checkExisted(dto.getStartTime(), dto.getEndTime(), dto.getImpactFrameId())) {
      resultInSideDto.setKey(RESULT.DUPLICATE);
      resultInSideDto.setMessage(I18n.getLanguage("crImpactFrame.unique"));
      return resultInSideDto;
    }
    CrImpactFrameEntity entity = dto.toEntity();
    getEntityManager().merge(entity);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("updateCrImpactFrame");
    logChangeConfigDTO.setContent("UpdateCrImpactFrame with ID: " + dto.getImpactFrameId());
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return resultInSideDto;
  }


  @Override
  public Datatable getListCrImpactFrame(CrImpactFrameInsiteDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_IMPACT_FRAME, "getListCrImpactFrame");
    String locale = I18n.getLocale();
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", locale);
    if (StringUtils.isNotNullOrEmpty(dto.getImpactFrameCode())) {
      sql += "  AND LOWER(list_result.impactFrameCode) LIKE '%' || :impactFrameCode || '%' ESCAPE '\\'";
      parameters.put("impactFrameCode",
          StringUtils.convertLowerParamContains(dto.getImpactFrameCode()));
    }
    if (StringUtils.isNotNullOrEmpty(dto.getImpactFrameName())) {
      sql += "  AND LOWER(list_result.impactFrameName) LIKE '%' || :impactFrameName || '%' ESCAPE '\\'";
      parameters.put("impactFrameName",
          StringUtils.convertLowerParamContains(dto.getImpactFrameName()));
    }
    if (StringUtils.isNotNullOrEmpty(dto.getDescription())) {
      sql += "  AND LOWER(list_result.description) LIKE '%' || :description || '%' ESCAPE '\\'";
      parameters.put("description",
          StringUtils.convertLowerParamContains(dto.getDescription()));
    }
    if (!StringUtils.isLongNullOrEmpty(dto.getIsActive())) {
      sql += " AND NVL(list_result.isActive,1) = :isActive ";
      parameters.put("isActive",
          dto.getIsActive());
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getStartTime())) {
      sql += " AND list_result.startDate LIKE '%' || :startTime || '%' ESCAPE '\\'";
      parameters.put("startTime",
          dto.getStartTime());
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getEndTime())) {
      sql += " AND list_result.endDate LIKE '%' || :endTime || '%' ESCAPE '\\'";
      parameters.put("endTime",
          dto.getEndTime());
    }
    if (StringUtils.isNotNullOrEmpty(dto.getSearchAll())) {
      sql += " AND (LOWER(list_result.impactFrameCode) LIKE '%' || :searchAll || '%' ESCAPE '\\'";
      sql += " OR LOWER(list_result.impactFrameName) LIKE '%' || :searchAll || '%' ESCAPE '\\')";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(dto.getSearchAll()));
    }
    sql += " ORDER BY list_result.impactFrameId DESC";
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(),
        dto.getPageSize(), CrImpactFrameInsiteDTO.class,
        dto.getSortName(),
        dto.getSortType());
  }

  public boolean checkExisted(String startTime, String endTime, Long id) {
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT IFE_ID impactFrameId "
        + "FROM OPEN_PM.CR_IMPACT_FRAME "
        + "WHERE START_TIME = :startTime "
        + "AND END_TIME     = :endTime ");
    parameters.put("startTime", startTime);
    parameters.put("endTime", endTime);
    List<CrImpactFrameInsiteDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrImpactFrameInsiteDTO.class));
    if (id != null) {
      for (int i = 0; i < list.size(); i++) {
        if (list.get(i).getImpactFrameId().equals(id)) {
          list.remove(i);
          break;
        }
      }
    }
    if (list != null && list.size() > 0) {
      return true;
    }
    return false;
  }
}
