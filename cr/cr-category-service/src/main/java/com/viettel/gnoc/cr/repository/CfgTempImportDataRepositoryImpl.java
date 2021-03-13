package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.cr.dto.TempImportDataDTO;
import com.viettel.gnoc.cr.model.TempImportDataEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import viettel.passport.client.UserToken;

@Repository
@Slf4j
public class CfgTempImportDataRepositoryImpl extends BaseRepository implements
    CfgTempImportDataRepository {

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public TempImportDataDTO findCfgTempImportDataById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(TempImportDataEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertTempImportData(TempImportDataDTO tempImportDataDTO) {
    TempImportDataEntity entity = tempImportDataDTO.toEntity();
    ResultInSideDto resultInSideDto = insertByModel(entity, colId);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("insertTempImportData");
    logChangeConfigDTO.setContent("InsertTempImportData with ID: " + resultInSideDto.getId());
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public String deleteTempImportDataById(Long id) {
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("deleteTempImportDataById");
    logChangeConfigDTO.setContent("DeleteTempImportDataById with ID: " + id);
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return deleteById(TempImportDataEntity.class, id, colId);
  }

  @Override
  public String updateTempImportData(TempImportDataDTO tempImportDataDTO) {
    TempImportDataEntity entity = tempImportDataDTO.toEntity();
    getEntityManager().merge(entity);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("updateTempImportData");
    logChangeConfigDTO.setContent("UpdateTempImportData with ID: " + tempImportDataDTO.getTidaId());
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return RESULT.SUCCESS;
  }

  @Override
  public Datatable getListTempImportData(TempImportDataDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_TEMP_IMPORT_DATA, "getListTempImportData");
    Map<String, Object> parameters = new HashMap<>();
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(),
        dto.getPageSize(), TempImportDataDTO.class,
        dto.getSortName(),
        dto.getSortType());
  }

  private static final String colId = "tidaId";
}
