package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrHardCDDTO;
import com.viettel.gnoc.maintenance.model.MrHardCdEntity;
import com.viettel.gnoc.wo.dto.WoCdGroupDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrHardCdRepositoryImpl extends BaseRepository implements MrHardCdRepository {

  @Override
  public Datatable getListMrHardCDPage(MrHardCDDTO mrHardCDDTO) {
    BaseDto baseDto = sqlSearch(mrHardCDDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        mrHardCDDTO.getPage(), mrHardCDDTO.getPageSize(),
        MrHardCDDTO.class,
        mrHardCDDTO.getSortName(), mrHardCDDTO.getSortType());
  }

  @Override
  public ResultInSideDto insertOrUpdate(MrHardCDDTO mrHardCDDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrHardCdEntity mrHardCdEntity = getEntityManager().merge(mrHardCDDTO.toEntity());
    resultInSideDto.setId(mrHardCdEntity.getHardCDId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long hardCDId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrHardCdEntity mrHardCdEntity = getEntityManager().find(MrHardCdEntity.class, hardCDId);
    getEntityManager().remove(mrHardCdEntity);
    return resultInSideDto;
  }

  @Override
  public MrHardCDDTO getDetail(Long hardCDId) {
    MrHardCdEntity mrHardCdEntity = getEntityManager().find(MrHardCdEntity.class, hardCDId);
    MrHardCDDTO mrHardCDDTO = mrHardCdEntity.toDTO();
    return mrHardCDDTO;
  }

  @Override
  public List<WoCdGroupDTO> getWoCdGroupCBB() {
    String sqlQuery = " select * from WFM.WO_CD_GROUP where IS_ENABLE = 1 ";
    Map<String, Object> parameters = new HashMap<>();
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters,
            BeanPropertyRowMapper.newInstance(WoCdGroupDTO.class));
  }

  @Override
  public List<MrHardCDDTO> getListDataExport(MrHardCDDTO mrHardCDDTO) {
    BaseDto baseDto = sqlSearch(mrHardCDDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrHardCDDTO.class));
  }

  public BaseDto sqlSearch(MrHardCDDTO mrHardCDDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_HARD_CD, "get-list-MrHardCD-page");
    Map<String, Object> parameters = new HashMap<>();
    if (mrHardCDDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(mrHardCDDTO.getCountryCode())) {
        sqlQuery += " and T1.COUNTRY LIKE :countryCode ";
        parameters
            .put("countryCode", mrHardCDDTO.getCountryCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrHardCDDTO.getCdId())) {
        sqlQuery += " and T1.CD_ID =:cdId ";
        parameters
            .put("cdId", mrHardCDDTO.getCdId());
      }
    }
    sqlQuery += " ORDER BY countryCode, regionName, stationCode, cdId, createUser ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
