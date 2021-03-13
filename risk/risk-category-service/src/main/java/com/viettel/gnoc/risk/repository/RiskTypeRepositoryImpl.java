package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.risk.dto.RiskTypeDTO;
import com.viettel.gnoc.risk.model.RiskTypeEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RiskTypeRepositoryImpl extends BaseRepository implements RiskTypeRepository {

  @Autowired
  protected RiskTypeDetailRepository riskTypeDetailRepository;

  @Override
  public Datatable getDataRiskTypeSearchWeb(RiskTypeDTO riskTypeDTO) {
    BaseDto baseDto = sqlDataRiskTypeSearchWeb(riskTypeDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        riskTypeDTO.getPage(), riskTypeDTO.getPageSize(), RiskTypeDTO.class,
        riskTypeDTO.getSortName(), riskTypeDTO.getSortType());
    return datatable;
  }

  @Override
  public List<RiskTypeDTO> getListRiskTypeDTO(RiskTypeDTO riskTypeDTO) {
    BaseDto baseDto = sqlDataRiskTypeSearchWeb(riskTypeDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(RiskTypeDTO.class));
  }

  @Override
  public RiskTypeDTO findRiskTypeByIdFromWeb(Long riskTypeId) {
    String leeLocale = I18n.getLocale();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_RISK_TYPE, "find-Risk-Type-By-Id-From-Web");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("systemRiskType", Constants.LANGUAGUE_EXCHANGE_SYSTEM.RISK);
    parameters.put("bussinessRiskType", Constants.APPLIED_BUSSINESS.RISK_TYPE);
    parameters.put("leeLocale", leeLocale);
    parameters.put("systemGroupType", Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON);
    parameters.put("bussinessGroupType", Constants.APPLIED_BUSSINESS.CAT_ITEM);
    parameters.put("inactive", I18n.getLanguage("riskType.status.inactive"));
    parameters.put("active", I18n.getLanguage("riskType.status.active"));
    parameters.put("riskTypeId", riskTypeId);
    List<RiskTypeDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(RiskTypeDTO.class));
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  private BaseDto sqlDataRiskTypeSearchWeb(RiskTypeDTO riskTypeDTO) {
    BaseDto baseDto = new BaseDto();
    String leeLocale = StringUtils.isNotNullOrEmpty(riskTypeDTO.getProxyLocale()) ? riskTypeDTO.getProxyLocale() : I18n.getLocale();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_RISK_TYPE, "get-List-Risk-Type-DTO");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("systemRiskType", Constants.LANGUAGUE_EXCHANGE_SYSTEM.RISK);
    parameters.put("bussinessRiskType", Constants.APPLIED_BUSSINESS.RISK_TYPE);
    parameters.put("leeLocale", leeLocale);
    parameters.put("systemGroupType", Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON);
    parameters.put("bussinessGroupType", Constants.APPLIED_BUSSINESS.CAT_ITEM);
    parameters.put("inactive", I18n.getLanguage("riskType.status.inactive"));
    parameters.put("active", I18n.getLanguage("riskType.status.active"));

    if (riskTypeDTO != null) {
      //Tim kiem nhanh
      if (StringUtils.isNotNullOrEmpty(riskTypeDTO.getSearchAll())) {
        sql += " AND (LOWER(t.RISK_TYPE_CODE) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(lt.RISK_TYPE_NAME) LIKE :searchAll ESCAPE '\\')";
        parameters
            .put("searchAll", StringUtils.convertLowerParamContains(riskTypeDTO.getSearchAll()));
      }
      if (StringUtils.isNotNullOrEmpty(riskTypeDTO.getRiskTypeCode())) {
        sql += " AND LOWER(t.RISK_TYPE_CODE) LIKE :riskTypeCode ESCAPE '\\'";
        parameters.put("riskTypeCode",
            StringUtils.convertLowerParamContains(riskTypeDTO.getRiskTypeCode()));
      }
      if (StringUtils.isNotNullOrEmpty(riskTypeDTO.getRiskTypeName())) {
        sql += " AND LOWER(lt.RISK_TYPE_NAME) LIKE :riskTypeName ESCAPE '\\'";
        parameters.put("riskTypeName",
            StringUtils.convertLowerParamContains(riskTypeDTO.getRiskTypeName()));
      }
      if (riskTypeDTO.getRiskGroupTypeId() != null) {
        sql += " AND t.RISK_GROUP_TYPE_ID = :riskGroupTypeId";
        parameters.put("riskGroupTypeId", riskTypeDTO.getRiskGroupTypeId());
      }
      if (riskTypeDTO.getStatus() != null) {
        sql += " AND t.STATUS = :status";
        parameters.put("status", riskTypeDTO.getStatus());
      }
    }
//    sql += " ORDER BY lt.RISK_TYPE_NAME ASC";
    sql += " ORDER BY t.RISK_TYPE_ID DESC";

    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public ResultInSideDto insertOrUpdateRiskType(RiskTypeDTO riskTypeDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    RiskTypeEntity riskTypeEntity = getEntityManager().merge(riskTypeDTO.toEntity());
    resultInSideDTO.setId(riskTypeEntity.getRiskTypeId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto delete(Long riskTypeId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    RiskTypeEntity riskTypeEntity = getEntityManager().find(RiskTypeEntity.class, riskTypeId);
    getEntityManager().remove(riskTypeEntity);
    return resultInSideDTO;
  }

  @Override
  public List<RiskTypeDTO> getListRiskTypeExport(RiskTypeDTO riskTypeDTO) {
    BaseDto baseDto = sqlDataRiskTypeSearchWeb(riskTypeDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(RiskTypeDTO.class));
  }

  @Override
  public RiskTypeDTO checkRiskTypeExit(RiskTypeDTO riskTypeDTO) {
    List<RiskTypeEntity> dataEntity = (List<RiskTypeEntity>) findByMultilParam(
        RiskTypeEntity.class, "riskTypeCode", riskTypeDTO.getRiskTypeCode());
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }
}
