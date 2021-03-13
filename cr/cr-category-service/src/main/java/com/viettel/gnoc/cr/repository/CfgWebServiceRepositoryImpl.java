package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.WebServiceDTO;
import com.viettel.gnoc.cr.model.WebServiceEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import viettel.passport.client.UserToken;

@Repository
@Slf4j
public class CfgWebServiceRepositoryImpl extends BaseRepository implements CfgWebServiceRepository {

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public WebServiceDTO findWebServiceById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(WebServiceEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertWebService(WebServiceDTO webServiceDTO) {
    WebServiceEntity entity = webServiceDTO.toEntity();
    ResultInSideDto resultInSideDto = insertByModel(entity, colId);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("insertWebService");
    logChangeConfigDTO.setContent("InsertWebService with ID: " + resultInSideDto.getId());
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public String deleteWebServiceById(Long id) {
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("deleteWebServiceById");
    logChangeConfigDTO.setContent("DeleteWebServiceById with ID: " + id);
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return deleteById(WebServiceEntity.class, id, colId);
  }

  @Override
  public String updateWebService(WebServiceDTO dto) {
    WebServiceEntity entity = dto.toEntity();
    getEntityManager().merge(entity);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("updateWebService");
    logChangeConfigDTO.setContent("UpdateWebService with ID: " + dto.getWebServiceId());
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return RESULT.SUCCESS;
  }

  @Override
  public Datatable getListWebService(WebServiceDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_WEBSERVICE, "getListWebService");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(dto.getWebServiceName())) {
      sql += "  AND LOWER(t3.WEBSERVICE_NAME) LIKE '%' || :webServiceName || '%' ESCAPE '\\'";
      parameters.put("webServiceName",
          StringUtils.convertLowerParamContains(dto.getWebServiceName()));
    }
    if (StringUtils.isNotNullOrEmpty(dto.getUrl())) {
      sql += "  AND LOWER(t3.URL) LIKE '%' || :url || '%' ESCAPE '\\'";
      parameters.put("url",
          StringUtils.convertLowerParamContains(dto.getUrl()));
    }
    if (StringUtils.isNotNullOrEmpty(dto.getNameSpaceURI())) {
      sql += "  AND LOWER(t3.NAMESPACE_URI) LIKE '%' || :nameSpaceURI || '%' ESCAPE '\\'";
      parameters.put("nameSpaceURI",
          StringUtils.convertLowerParamContains(dto.getNameSpaceURI()));
    }
    if (StringUtils.isNotNullOrEmpty(dto.getLocalPart())) {
      sql += "  AND LOWER(t3.LOCALPART) LIKE '%' || :localPart || '%' ESCAPE '\\'";
      parameters.put("localPart",
          StringUtils.convertLowerParamContains(dto.getLocalPart()));
    }
    if (StringUtils.isNotNullOrEmpty(dto.getWebServiceClassPath())) {
      sql += "  AND LOWER(t3.WEBSERVICE_CLASS_PATH) LIKE '%' || :webServiceClassPath || '%' ESCAPE '\\'";
      parameters.put("webServiceClassPath",
          StringUtils.convertLowerParamContains(dto.getWebServiceClassPath()));
    }
    if (StringUtils.isNotNullOrEmpty(dto.getGetPortMethod())) {
      sql += "  AND LOWER(t3.GET_PORT_METHOD) LIKE '%' || :getPortMethod || '%' ESCAPE '\\'";
      parameters.put("getPortMethod",
          StringUtils.convertLowerParamContains(dto.getGetPortMethod()));
    }
    if (!StringUtils.isLongNullOrEmpty(dto.getIsActive())) {
      sql += "  AND  NVL(t3.IS_ACTIVE,1) = :isActive ";
      parameters.put("isActive",
          dto.getIsActive());
    }
    if (StringUtils.isNotNullOrEmpty(dto.getSearchAll())) {
      sql += " AND (LOWER(t3.WEBSERVICE_NAME) LIKE '%' || :searchAll || '%' ESCAPE '\\')";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(dto.getSearchAll()));
    }
    sql += " ORDER BY t3.WEBSERVICE_ID DESC";
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(),
        dto.getPageSize(), WebServiceDTO.class,
        dto.getSortName(),
        dto.getSortType());
  }

  private static final String colId = "webServiceId";
}
