package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRStatusDTO;
import com.viettel.gnoc.sr.model.SRStatusEntity;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SRStatusRepositoryImpl extends BaseRepository implements SRStatusRepository {

  @Override
  public Datatable getListSRStatusPage(SRStatusDTO srStatusDTO) {
    BaseDto baseDto = sqlSearch(srStatusDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        srStatusDTO.getPage(), srStatusDTO.getPageSize(),
        SRStatusDTO.class,
        srStatusDTO.getSortName(), srStatusDTO.getSortType());
  }

  @Override
  public ResultInSideDto add(SRStatusDTO srStatusDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRStatusEntity srStatusEntity = getEntityManager().merge(srStatusDTO.toEntity());
    resultInSideDto.setId(srStatusEntity.getConfigId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto edit(SRStatusDTO srStatusDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(srStatusDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public SRStatusDTO getDetail(Long configId) {
    SRStatusEntity srStatusEntity = getEntityManager().find(SRStatusEntity.class, configId);
    SRStatusDTO srStatusDTO = srStatusEntity.toDTO();
    return srStatusDTO;
  }

  public BaseDto sqlSearch(SRStatusDTO srStatusDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_STATUS, "sr-status-list");
    Map<String, Object> parameters = new HashMap<>();
    if (srStatusDTO != null) {
      if (StringUtils.isNotNullOrEmpty(srStatusDTO.getSearchAll())) {
        sqlQuery += " AND (lower(CONFIG_CODE) LIKE :searchAll ESCAPE '\\' OR lower(CONFIG_NAME) LIKE :searchAll ESCAPE '\\')";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(srStatusDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(srStatusDTO.getConfigCode())) {
        sqlQuery += " AND (lower(CONFIG_CODE) LIKE :configCode ESCAPE '\\') ";
        parameters
            .put("configCode", StringUtils.convertLowerParamContains(srStatusDTO.getConfigCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(srStatusDTO.getConfigName())) {
        sqlQuery += " AND (lower(CONFIG_NAME) LIKE :configName ESCAPE '\\') ";
        parameters
            .put("configName", StringUtils.convertLowerParamContains(srStatusDTO.getConfigName()));
      }
      if (!StringUtils.isStringNullOrEmpty(srStatusDTO.getStatus())) {
        sqlQuery += " AND STATUS= :status ";
        parameters.put("status", srStatusDTO.getStatus());
      }
    }
    sqlQuery += " ORDER BY config_Id DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
