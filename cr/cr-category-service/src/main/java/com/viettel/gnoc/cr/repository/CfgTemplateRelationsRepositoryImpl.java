package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.cr.dto.TemplateRelationsDTO;
import com.viettel.gnoc.cr.model.TemplateRelationsEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import viettel.passport.client.UserToken;

@Repository
@Slf4j
public class CfgTemplateRelationsRepositoryImpl extends BaseRepository implements
    CfgTemplateRelationsRepository {

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public String updateTemplateRelations(TemplateRelationsDTO dto) {
    TemplateRelationsEntity entity = dto.toEntity();
    getEntityManager().merge(entity);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("updateTemplateRelations");
    logChangeConfigDTO.setContent("UpdateTemplateRelations with ID: " + dto.getTrsId());
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return RESULT.SUCCESS;
  }

  @Override
  public TemplateRelationsDTO findTemplateRelationsById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(TemplateRelationsEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertTemplateRelations(TemplateRelationsDTO dto) {
    TemplateRelationsEntity entity = dto.toEntity();
    ResultInSideDto resultInSideDto = insertByModel(entity, colId);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("insertTemplateRelations");
    logChangeConfigDTO.setContent("InsertTemplateRelations with ID: " + resultInSideDto.getId());
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public String deleteTemplateRelationsById(Long id) {
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("deleteTemplateRelationsById");
    logChangeConfigDTO.setContent("DeleteTemplateRelationsById with ID: " + id);
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return deleteById(TemplateRelationsEntity.class, id, colId);
  }

  @Override
  public Datatable getListTemplateRelations(TemplateRelationsDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_TEMP_RELATIONS, "getListTemplateRelations");
    Map<String, Object> parameters = new HashMap<>();
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(),
        dto.getPageSize(), TemplateRelationsDTO.class,
        dto.getSortName(),
        dto.getSortType());
  }

  private static final String colId = "trsId";

}
