package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrITSoftCatMarketDTO;
import com.viettel.gnoc.maintenance.model.MrITSoftCatMarketEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrITSoftCatMarketRepositoryImpl extends BaseRepository implements
    MrITSoftCatMarketRepository {

  @Override
  public Datatable getListMrCatMarketSearch(MrITSoftCatMarketDTO mrITSoftCatMarketDTO) {
    BaseDto baseDto = sqlSearch(mrITSoftCatMarketDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrITSoftCatMarketDTO.getPage(),
        mrITSoftCatMarketDTO.getPageSize(), MrITSoftCatMarketDTO.class,
        mrITSoftCatMarketDTO.getSortName(), mrITSoftCatMarketDTO.getSortType());
  }

  @Override
  public List<MrITSoftCatMarketDTO> getListMrCatMarketSearchNoPage(
      MrITSoftCatMarketDTO mrITSoftCatMarketDTO) {
    BaseDto baseDto = sqlSearch(mrITSoftCatMarketDTO);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrITSoftCatMarketDTO.class));
  }

  public BaseDto sqlSearch(MrITSoftCatMarketDTO mrITSoftCatMarketDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SOFT_CNTT_CAT_MARKET,
            "getListMrSoftCNTTCatMarket");
    Map<String, Object> parameters = new HashMap<>();
    if (mrITSoftCatMarketDTO != null) {
      if (StringUtils.isNotNullOrEmpty(mrITSoftCatMarketDTO.getSearchAll())) {
        sqlQuery += " AND UPPER(a.MARKET_CODE) LIKE :searhAll ESCAPE '\\' OR UPPER(a.MARKET_NAME) LIKE :searhAll ESCAPE '\\' ";
        parameters.put("searhAll",
            StringUtils.convertUpperParamContains(mrITSoftCatMarketDTO.getSearchAll()));
      }
      if (StringUtils.isNotNullOrEmpty(mrITSoftCatMarketDTO.getMarketCode())) {
        sqlQuery += " AND UPPER(a.MARKET_CODE) LIKE :marketCode ESCAPE '\\'";
        parameters.put("marketCode",
            StringUtils.convertUpperParamContains(mrITSoftCatMarketDTO.getMarketCode()));
      }
      if (StringUtils.isNotNullOrEmpty(mrITSoftCatMarketDTO.getMarketName())) {
        sqlQuery += "  AND UPPER(a.MARKET_NAME) LIKE :marketName ESCAPE '\\'";
        parameters.put("marketName",
            StringUtils.convertUpperParamContains(mrITSoftCatMarketDTO.getMarketName()));
      }
      if (StringUtils.isNotNullOrEmpty(mrITSoftCatMarketDTO.getCountryCode())) {
        sqlQuery += "  AND a.COUNTRY =:country ";
        parameters.put("country", mrITSoftCatMarketDTO.getCountryCode());
      }
    }
    sqlQuery += " ORDER BY a.MARKET_CODE ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public ResultInSideDto insert(MrITSoftCatMarketDTO mrITSoftCatMarketDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    String sql = "";
    Map<String, Object> parameters = new HashMap<>();
    if (mrITSoftCatMarketDTO != null) {
      sql = "INSERT INTO MR_CAT_MARKET (MARKET_CODE, MARKET_NAME, COUNTRY) VALUES (:marketCode, :marketName, :countryCode) ";
      parameters.put("marketCode", mrITSoftCatMarketDTO.getMarketCode());
      parameters.put("marketName", mrITSoftCatMarketDTO.getMarketName());
      parameters.put("countryCode", mrITSoftCatMarketDTO.getCountryCode());

    }
    int row = getNamedParameterJdbcTemplate().update(sql, parameters);
    resultInSideDto.setQuantitySucc(row);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto update(MrITSoftCatMarketDTO mrITSoftCatMarketDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    String sql = "";
    Map<String, Object> parameters = new HashMap<>();
    if (mrITSoftCatMarketDTO != null) {
      sql = "UPDATE MR_CAT_MARKET SET MARKET_NAME = :marketName,COUNTRY = :countryCode  where MARKET_CODE = :marketCode ";
      parameters.put("marketCode", mrITSoftCatMarketDTO.getMarketCode());
      parameters.put("marketName", mrITSoftCatMarketDTO.getMarketName());
      parameters.put("countryCode", mrITSoftCatMarketDTO.getCountryCode());

    }
    int row = getNamedParameterJdbcTemplate().update(sql, parameters);
    resultInSideDto.setQuantitySucc(row);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(String marketCode) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    String sql = "";
    Map<String, Object> parameters = new HashMap<>();
    sql = "delete from MR_CAT_MARKET where MARKET_CODE = :marketCode ";
    parameters.put("marketCode", marketCode);
    int row = getNamedParameterJdbcTemplate().update(sql, parameters);
    resultInSideDto.setQuantitySucc(row);
    return resultInSideDto;
  }

  public MrITSoftCatMarketDTO checkExist(String marketCode) {

    List<MrITSoftCatMarketEntity> dataEntity = (List<MrITSoftCatMarketEntity>) findByMultilParam(
        MrITSoftCatMarketEntity.class,
        "marketCode", marketCode);

    if (dataEntity.size() != 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }
}
