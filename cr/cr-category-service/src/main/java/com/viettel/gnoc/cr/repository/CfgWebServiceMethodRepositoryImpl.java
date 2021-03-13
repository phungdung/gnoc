package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.cr.dto.WebServiceDTO;
import com.viettel.gnoc.cr.dto.WebServiceMethodDTO;
import com.viettel.gnoc.cr.model.WebServiceEntity;
import com.viettel.gnoc.cr.model.WebServiceMethodEntity;
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
public class CfgWebServiceMethodRepositoryImpl extends BaseRepository implements
    CfgWebServiceMethodRepository {

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public WebServiceMethodDTO findWebServiceMethodById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(WebServiceMethodEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertWebServiceMethod(WebServiceMethodDTO webServiceMethodDTO) {
    WebServiceMethodEntity entity = webServiceMethodDTO.toEntity();
    ResultInSideDto resultInSideDto = insertByModel(entity, colId);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("insertWebServiceMethod");
    logChangeConfigDTO.setContent("InsertWebServiceMethod with ID: " + resultInSideDto.getId());
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public String deleteWebServiceMethodById(Long id) {
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("deleteWebServiceMethodById");
    logChangeConfigDTO.setContent("DeleteWebServiceMethodById with ID: " + id);
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return deleteById(WebServiceMethodEntity.class, id, colId);
  }

  @Override
  public String updateWebServiceMethod(WebServiceMethodDTO dto) {
    WebServiceMethodEntity entity = dto.toEntity();
    getEntityManager().merge(entity);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("updateWebServiceMethod");
    logChangeConfigDTO.setContent("UpdateWebServiceMethod with ID: " + dto.getWebServiceMethodId());
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return RESULT.SUCCESS;
  }

  @Override
  public List<WebServiceMethodDTO> getListWebServiceMethod() {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_WEBSERVICE_METHOD, "getListWebServiceMethod");
    Map<String, Object> parameters = new HashMap<>();
    List<WebServiceMethodDTO> webServiceMethodDTOS = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper
            .newInstance(WebServiceMethodDTO.class));
    return webServiceMethodDTOS;
  }

  @Override
  public String deleteListWebServiceMethodByWebServiceId(List<WebServiceMethodDTO> dto) {
    return deleteByListDTO(dto, WebServiceMethodEntity.class, colWebServiceId);
  }

  @Override
  public WebServiceDTO findWebServiceById(Long id) {
    List<WebServiceMethodDTO> webServiceMethodDTOS = null;
    if (id != null && id > 0) {
      webServiceMethodDTOS = getWebServiceMethodDTOS(id);
    }
    WebServiceDTO webServiceDTO = getEntityManager().find(WebServiceEntity.class, id).toDTO();
    webServiceDTO.setWebServiceMethodDTOS(webServiceMethodDTOS);
    return webServiceDTO;
  }

  @Override
  public List<WebServiceMethodDTO> getWebServiceMethodDTOS(Long id) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_WEBSERVICE, "findWebServiceMethodById");
    parameters.put("webServiceId", id);
    List<WebServiceMethodDTO> webServiceMethodDTOS = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WebServiceMethodDTO.class));
    return webServiceMethodDTOS;
  }

  private static final String colId = "webServiceMethodId";
  private static final String colWebServiceId = "webServiceId";
}
