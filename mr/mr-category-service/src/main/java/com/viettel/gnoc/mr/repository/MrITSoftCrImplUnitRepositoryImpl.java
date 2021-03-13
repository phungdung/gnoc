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
import com.viettel.gnoc.maintenance.dto.MrITSoftCrImplUnitDTO;
import com.viettel.gnoc.maintenance.model.MrITSoftCrImplUnitEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrITSoftCrImplUnitRepositoryImpl extends BaseRepository implements
    MrITSoftCrImplUnitRepository {

  @Override
  public Datatable getListDataMrCfgCrUnitITSoft(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO) {
    BaseDto baseDto = sqlSearch(mrITSoftCrImplUnitDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrITSoftCrImplUnitDTO.getPage(), mrITSoftCrImplUnitDTO.getPageSize(),
        MrITSoftCrImplUnitDTO.class, mrITSoftCrImplUnitDTO.getSortName(),
        mrITSoftCrImplUnitDTO.getSortType());
    return datatable;
  }

  @Override
  public ResultInSideDto insertOrUpdateMrCfgCrUnitIT(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrITSoftCrImplUnitEntity entity = getEntityManager().merge(mrITSoftCrImplUnitDTO.toModel());
    resultInSideDto.setId(entity.getCfgId());
    return resultInSideDto;
  }

  @Override
  public MrITSoftCrImplUnitDTO findMrCfgCrUnitITById(Long cfgId) {
    MrITSoftCrImplUnitEntity entity = getEntityManager()
        .find(MrITSoftCrImplUnitEntity.class, cfgId);
    if (entity != null) {
      return entity.toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteMrCfgCrUnitIT(Long cfgId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    MrITSoftCrImplUnitEntity entity = getEntityManager()
        .find(MrITSoftCrImplUnitEntity.class, cfgId);
    getEntityManager().remove(entity);
    return resultInSideDTO;
  }

  @Override
  public List<MrITSoftCrImplUnitDTO> getDataExport(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO) {
    BaseDto baseDto = sqlSearch(mrITSoftCrImplUnitDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrITSoftCrImplUnitDTO.class));
  }


  private BaseDto sqlSearch(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO) {
    BaseDto baseDto = new BaseDto();
    String leeLocale = I18n.getLocale();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CFG_CR_UNIT_IT,
            "sql-Get-List-Mr-Cfg-Cr-Unit-IT");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("systemArray", LANGUAGUE_EXCHANGE_SYSTEM.COMMON);
    parameters.put("bussinessArray", APPLIED_BUSSINESS.CAT_ITEM.toString());
    parameters.put("leeLocale", leeLocale);
    parameters.put("categoryCode", MR_ITEM_NAME.MR_SUBCATEGORY);
    if (mrITSoftCrImplUnitDTO != null) {
      if (StringUtils.isNotNullOrEmpty(mrITSoftCrImplUnitDTO.getMarketCode())) {
        sql += " AND t1.MARKET_CODE = :marketCode";
        parameters.put("marketCode", mrITSoftCrImplUnitDTO.getMarketCode());
      }
      if (StringUtils.isNotNullOrEmpty(mrITSoftCrImplUnitDTO.getDeviceTypeId())) {
        sql += " AND t1.DEVICE_TYPE_ID = :deviceTypeId";
        parameters.put("deviceTypeId", mrITSoftCrImplUnitDTO.getDeviceTypeId());
      }
      if (StringUtils.isNotNullOrEmpty(mrITSoftCrImplUnitDTO.getArrayCode())) {
        sql += " AND t1.ARRAY_CODE = :arrayCode";
        parameters.put("arrayCode", mrITSoftCrImplUnitDTO.getArrayCode());
      }

      if (mrITSoftCrImplUnitDTO.getImplementUnit() != null) {
        sql += " AND t1.IMPLEMENT_UNIT = :implementUnit";
        parameters.put("implementUnit", mrITSoftCrImplUnitDTO.getImplementUnit());
      }
      if (mrITSoftCrImplUnitDTO.getCheckingUnit() != null) {
        sql += " AND t1.CHECKING_UNIT = :checkingUnit";
        parameters.put("checkingUnit", mrITSoftCrImplUnitDTO.getCheckingUnit());
      }
      if (StringUtils.isNotNullOrEmpty(mrITSoftCrImplUnitDTO.getRegion())) {
        sql += " AND t1.REGION = :region";
        parameters.put("region", mrITSoftCrImplUnitDTO.getRegion());
      }
    }
    sql += " ORDER BY t1.MARKET_CODE";

    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public MrITSoftCrImplUnitDTO checkMrCfgCrUnitITExit(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO) {
    List<MrITSoftCrImplUnitEntity> dataEntity = (List<MrITSoftCrImplUnitEntity>) findByMultilParam(
        MrITSoftCrImplUnitEntity.class, "marketCode", mrITSoftCrImplUnitDTO.getMarketCode(),
        "region", mrITSoftCrImplUnitDTO.getRegion(),
        "arrayCode", mrITSoftCrImplUnitDTO.getArrayCode(),
        "deviceTypeId", mrITSoftCrImplUnitDTO.getDeviceTypeId());
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }


}
