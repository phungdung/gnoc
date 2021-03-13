package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrActionCodeDTO;
import com.viettel.gnoc.cr.model.CrActionCodeEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import viettel.passport.client.UserToken;

@Repository
@Slf4j
public class CfgCrActionCodeRepositoryImpl extends BaseRepository implements
    CfgCrActionCodeRepository {

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public CrActionCodeDTO findCrActionCodeById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(CrActionCodeEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertCfgCrActionCode(CrActionCodeDTO crActionCodeDTO) {
    CrActionCodeEntity entity = crActionCodeDTO.toEntity();
    ResultInSideDto resultInSideDto = insertByModel(entity, colId);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("Insert Config Cr Action Code");
    logChangeConfigDTO
        .setContent("Insert Config Cr Action Code with ID: " + resultInSideDto.getId());
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public String deleteCfgCrActionCodeById(Long id) {
    CrActionCodeDTO crActionCodeDTO = findCrActionCodeById(id);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("deleteCfgCrActionCodeById");
    logChangeConfigDTO.setContent("DeleteCfgCrActionCodeById with ID: " + id);
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    crActionCodeDTO.setIsActive(0L);
    getEntityManager().merge(crActionCodeDTO.toEntity());
    return RESULT.SUCCESS;
  }

  @Override
  public String updateCfgCrActionCode(CrActionCodeDTO dto) {
    CrActionCodeEntity entity = dto.toEntity();
    getEntityManager().merge(entity);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("updateCfgCrActionCode");
    logChangeConfigDTO.setContent("UpdateCfgCrActionCode: " + dto.getCrActionCodeId());
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return RESULT.SUCCESS;
  }

  @Override
  public Datatable getListCfgCrActionCode(CrActionCodeDTO crActionCodeDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_ACTION_CODE, "getListCfgCrActionCode");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(crActionCodeDTO.getCrActionCode())) {
      sql += " AND LOWER(t1.CR_ACTION_CODE) LIKE '%' || :crActionCode || '%' ESCAPE '\\'";
      parameters.put("crActionCode",
          StringUtils.convertLowerParamContains(crActionCodeDTO.getCrActionCode()));
    }
    if (StringUtils.isNotNullOrEmpty(crActionCodeDTO.getCrActionCodeTittle())) {
      sql += " AND LOWER(t1.CR_ACTION_CODE_TITTLE) LIKE '%' || :crActionCodeTitle || '%' ESCAPE '\\'";
      parameters.put("crActionCodeTitle",
          StringUtils.convertLowerParamContains(crActionCodeDTO.getCrActionCodeTittle()));
    }
    if (!StringUtils.isLongNullOrEmpty(crActionCodeDTO.getIsActive())) {
      sql += " AND t1.IS_ACTIVE = :isActive ";
      parameters.put("isActive",
          crActionCodeDTO.getIsActive());
    }
    if (StringUtils.isNotNullOrEmpty(crActionCodeDTO.getSearchAll())) {
      sql += " AND (LOWER(t1.CR_ACTION_CODE) LIKE '%' || :searchAll || '%' ESCAPE '\\')";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(crActionCodeDTO.getSearchAll()));
    }
    sql += " ORDER BY t1.CR_ACTION_CODE_ID DESC ";
    return getListDataTableBySqlQuery(sql, parameters, crActionCodeDTO.getPage(),
        crActionCodeDTO.getPageSize(), CrActionCodeDTO.class,
        crActionCodeDTO.getSortName(),
        crActionCodeDTO.getSortType());
  }

  private static final String colId = "crActionCodeId";

}
