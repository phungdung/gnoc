package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.risk.dto.RiskChangeStatusDTO;
import com.viettel.gnoc.risk.model.RiskChangeStatusEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RiskChangeStatusRepositoryImpl extends BaseRepository implements
    RiskChangeStatusRepository {

  @Override
  public Datatable getDataRiskChangeStatusSearchWeb(RiskChangeStatusDTO riskChangeStatusDTO) {
    BaseDto baseDto = sqlDataRiskChangeStatusSearchWeb(riskChangeStatusDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        riskChangeStatusDTO.getPage(), riskChangeStatusDTO.getPageSize(), RiskChangeStatusDTO.class,
        riskChangeStatusDTO.getSortName(), riskChangeStatusDTO.getSortType());
    return datatable;
  }

  @Override
  public List<RiskChangeStatusDTO> onSearch(RiskChangeStatusDTO riskChangeStatusDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(RiskChangeStatusEntity.class, riskChangeStatusDTO, rowStart, maxRow,
        sortType, sortFieldList);
  }

  @Override
  public ResultInSideDto insertOrUpdateRiskChangeStatus(RiskChangeStatusDTO riskChangeStatusDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    RiskChangeStatusEntity entity = getEntityManager().merge(riskChangeStatusDTO.toEntity());
    resultInSideDTO.setId(entity.getId());
    return resultInSideDTO;
  }

  @Override
  public RiskChangeStatusDTO findRiskChangeStatusById(Long id) {
    RiskChangeStatusEntity entity = getEntityManager().find(RiskChangeStatusEntity.class, id);
    if (entity != null) {
      return entity.toDTO();
    }
    return null;
  }

  @Override
  public List<RiskChangeStatusDTO> getListRiskChangeStatusExport(
      RiskChangeStatusDTO riskChangeStatusDTO) {
    BaseDto baseDto = sqlDataRiskChangeStatusSearchWeb(riskChangeStatusDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(RiskChangeStatusDTO.class));
  }

  @Override
  public ResultInSideDto deleteRiskChangeStatus(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    RiskChangeStatusEntity entity = getEntityManager().find(RiskChangeStatusEntity.class, id);
    getEntityManager().remove(entity);
    return resultInSideDto;
  }

  private BaseDto sqlDataRiskChangeStatusSearchWeb(RiskChangeStatusDTO riskChangeStatusDTO) {
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_RISK_CHANGE_STATUS,
            "get-Data-Risk-Change-Status-Search-Web");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("ok", I18n.getLanguage("riskChangeStatus.isDefault.ok"));
    parameters.put("nok", I18n.getLanguage("riskChangeStatus.isDefault.nok"));
    parameters.put("categoryCode", Constants.CATEGORY.RISK_STATUS);

    if (riskChangeStatusDTO != null) {
      //Tim kiem nhanh
      if (StringUtils.isNotNullOrEmpty(riskChangeStatusDTO.getSearchAll())) {
        sql += " AND (LOWER(t.RISK_TYPE_NAME) LIKE :searchAll ESCAPE '\\')";
        parameters.put("searchAll",
            StringUtils.convertLowerParamContains(riskChangeStatusDTO.getSearchAll()));
      }
      if (riskChangeStatusDTO.getRiskTypeId() != null) {
        sql += " AND c.RISK_TYPE_ID = :riskTypeId";
        parameters.put("riskTypeId", riskChangeStatusDTO.getRiskTypeId());
      }
      if (riskChangeStatusDTO.getRiskPriority() != null) {
        sql += " AND c.RISK_PRIORITY = :riskPriority";
        parameters.put("riskPriority", riskChangeStatusDTO.getRiskPriority());
      }
      if (riskChangeStatusDTO.getOldStatus() != null) {
        sql += " AND c.OLD_STATUS = :oldStatus";
        parameters.put("oldStatus", riskChangeStatusDTO.getOldStatus());
      }
      if (riskChangeStatusDTO.getNewStatus() != null) {
        sql += " AND c.NEW_STATUS = :newStatus";
        parameters.put("newStatus", riskChangeStatusDTO.getNewStatus());
      }
      if (riskChangeStatusDTO.getIsDefault() != null) {
        sql += " AND c.IS_DEFAULT = :isDefault";
        parameters.put("isDefault", riskChangeStatusDTO.getIsDefault());
      }
    }
    sql += " ORDER BY c.ID ASC";
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
