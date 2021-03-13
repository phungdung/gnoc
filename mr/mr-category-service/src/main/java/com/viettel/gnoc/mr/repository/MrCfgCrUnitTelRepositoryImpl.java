package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.MR_ITEM_NAME;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCfgCrUnitTelDTO;
import com.viettel.gnoc.maintenance.model.MrCfgCrUnitTelEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrCfgCrUnitTelRepositoryImpl extends BaseRepository implements
    MrCfgCrUnitTelRepository {

  @Override
  public Datatable getListDataMrCfgCrUnitTelSearchWeb(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO) {
    BaseDto baseDto = sqlGetListMrCfgCrUnitTelSearchWeb(mrCfgCrUnitTelDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrCfgCrUnitTelDTO.getPage(), mrCfgCrUnitTelDTO.getPageSize(),
        MrCfgCrUnitTelDTO.class, mrCfgCrUnitTelDTO.getSortName(),
        mrCfgCrUnitTelDTO.getSortType());
    return datatable;
  }

  @Override
  public ResultInSideDto insertMrCfgCrUnitTel(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().persist(mrCfgCrUnitTelDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateMrCfgCrUnitTel(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(mrCfgCrUnitTelDTO.toEntity());
    resultInSideDto.setId(mrCfgCrUnitTelDTO.getCfgId());
    return resultInSideDto;
  }

  @Override
  public MrCfgCrUnitTelDTO findMrCfgCrUnitTelById(Long cfgId) {
    MrCfgCrUnitTelEntity entity = getEntityManager().find(MrCfgCrUnitTelEntity.class, cfgId);
    if (entity != null) {
      return entity.toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteMrCfgCrUnitTel(Long cfgId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    MrCfgCrUnitTelEntity entity = getEntityManager().find(MrCfgCrUnitTelEntity.class, cfgId);
    getEntityManager().remove(entity);
    return resultInSideDTO;
  }

  @Override
  public List<MrCfgCrUnitTelDTO> getListMrCfgCrUnitTelExport(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO) {
    BaseDto baseDto = sqlGetListMrCfgCrUnitTelSearchWeb(mrCfgCrUnitTelDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrCfgCrUnitTelDTO.class));
  }

  @Override
  public MrCfgCrUnitTelDTO checkMrCfgCrUnitTelExit(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO) {
    List<MrCfgCrUnitTelEntity> dataEntity = (List<MrCfgCrUnitTelEntity>) findByMultilParam(
        MrCfgCrUnitTelEntity.class, "marketCode", mrCfgCrUnitTelDTO.getMarketCode(),
        "region", mrCfgCrUnitTelDTO.getRegion(),
        "arrayCode", mrCfgCrUnitTelDTO.getArrayCode(),
        "networkType", mrCfgCrUnitTelDTO.getNetworkType(),
        "deviceType", mrCfgCrUnitTelDTO.getDeviceType());
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public List<MrCfgCrUnitTelDTO> getListMrCfgCrUnitTel(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO) {
    BaseDto baseDto = sqlGetListMrCfgCrUnitTelSearchWeb(mrCfgCrUnitTelDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrCfgCrUnitTelDTO.class));
  }

  @Override
  public MrCfgCrUnitTelDTO findMrCfgCrUnitTelSoftWeb(Long cfgId) {
    String leeLocale = I18n.getLocale();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CFG_CR_UNIT_TEL,
            "sql-Get-List-Mr-Cfg-Cr-Unit-Tel-Search-Web");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("systemArray", LANGUAGUE_EXCHANGE_SYSTEM.COMMON);
    parameters.put("bussinessArray", APPLIED_BUSSINESS.CAT_ITEM.toString());
    parameters.put("leeLocale", leeLocale);
    parameters.put("categoryCode", MR_ITEM_NAME.MR_SUBCATEGORY);
    sql += " AND t.CFG_ID = :cfgId";
    parameters.put("cfgId", cfgId);
    List<MrCfgCrUnitTelDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrCfgCrUnitTelDTO.class));
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  private BaseDto sqlGetListMrCfgCrUnitTelSearchWeb(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO) {
    BaseDto baseDto = new BaseDto();
    String leeLocale = I18n.getLocale();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CFG_CR_UNIT_TEL,
            "sql-Get-List-Mr-Cfg-Cr-Unit-Tel-Search-Web");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("systemArray", LANGUAGUE_EXCHANGE_SYSTEM.COMMON);
    parameters.put("bussinessArray", APPLIED_BUSSINESS.CAT_ITEM.toString());
    parameters.put("leeLocale", leeLocale);
    parameters.put("categoryCode", MR_ITEM_NAME.MR_SUBCATEGORY);
    if (mrCfgCrUnitTelDTO != null) {
      if (StringUtils.isNotNullOrEmpty(mrCfgCrUnitTelDTO.getMarketCode())) {
        sql += " AND t.MARKET_CODE = :marketCode";
        parameters.put("marketCode", mrCfgCrUnitTelDTO.getMarketCode());
      }
      if (StringUtils.isNotNullOrEmpty(mrCfgCrUnitTelDTO.getDeviceType())) {
        sql += " AND t.DEVICE_TYPE = :deviceType";
        parameters.put("deviceType", mrCfgCrUnitTelDTO.getDeviceType());
      }
      if (StringUtils.isNotNullOrEmpty(mrCfgCrUnitTelDTO.getArrayCode())) {
        sql += " AND t.ARRAY_CODE = :arrayCode";
        parameters.put("arrayCode", mrCfgCrUnitTelDTO.getArrayCode());
      }
      if (StringUtils.isNotNullOrEmpty(mrCfgCrUnitTelDTO.getNetworkType())) {
        sql += " AND t.NETWORK_TYPE = :networkType";
        parameters.put("networkType", mrCfgCrUnitTelDTO.getNetworkType());
      }
      if (mrCfgCrUnitTelDTO.getImplementUnit() != null) {
        sql += " AND t.IMPLEMENT_UNIT = :implementUnit";
        parameters.put("implementUnit", mrCfgCrUnitTelDTO.getImplementUnit());
      }
      if (mrCfgCrUnitTelDTO.getCheckingUnit() != null) {
        sql += " AND t.CHECKING_UNIT = :checkingUnit";
        parameters.put("checkingUnit", mrCfgCrUnitTelDTO.getCheckingUnit());
      }
      if (StringUtils.isNotNullOrEmpty(mrCfgCrUnitTelDTO.getRegion())) {
        sql += " AND t.REGION = :region";
        parameters.put("region", mrCfgCrUnitTelDTO.getRegion());
      }
    }
    sql += " ORDER BY t.MARKET_CODE";

    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
