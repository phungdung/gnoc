package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.CfgMapUnitGnocNimsDTO;
import com.viettel.gnoc.wo.model.CfgMapUnitGnocNimsEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgMapUnitGnocNimsRepositoryImpl extends BaseRepository implements
    CfgMapUnitGnocNimsRepository {

  private final static String DATA_EXPORT = "DATA_EXPORT";

  @Override
  public Datatable getListCfgMapUnitGnocNimsPage(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO) {
    BaseDto baseDto = sqlSearch(cfgMapUnitGnocNimsDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        cfgMapUnitGnocNimsDTO.getPage(), cfgMapUnitGnocNimsDTO.getPageSize(),
        CfgMapUnitGnocNimsDTO.class,
        cfgMapUnitGnocNimsDTO.getSortName(), cfgMapUnitGnocNimsDTO.getSortType());
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgMapUnitGnocNimsEntity cfgMapUnitGnocNimsEntity = getEntityManager()
        .find(CfgMapUnitGnocNimsEntity.class, id);
    getEntityManager().remove(cfgMapUnitGnocNimsEntity);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertImport(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    CfgMapUnitGnocNimsEntity cfgMapUnitGnocNimsEntity = cfgMapUnitGnocNimsDTO.toEntity();
    if (cfgMapUnitGnocNimsEntity.getId() != null) {
      getEntityManager().merge(cfgMapUnitGnocNimsEntity);
    } else {
      getEntityManager().persist(cfgMapUnitGnocNimsEntity);
    }
    resultInSideDTO.setId(cfgMapUnitGnocNimsEntity.getId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto insertList(List<CfgMapUnitGnocNimsDTO> cfgMapUnitGnocNimsDTOList) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (CfgMapUnitGnocNimsDTO dto : cfgMapUnitGnocNimsDTOList) {
      resultInSideDto = insertImport(dto);
    }
    return resultInSideDto;
  }

  @Override
  public Datatable getListDataExport(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO) {
    BaseDto baseDto = sqlSearch(cfgMapUnitGnocNimsDTO);
    Datatable datatable = new Datatable();
    List<CfgMapUnitGnocNimsDTO> cfgMapUnitGnocNimsDTOList = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(CfgMapUnitGnocNimsDTO.class));

    datatable.setData(cfgMapUnitGnocNimsDTOList);
    return datatable;
  }

  @Override
  public ResultInSideDto add(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgMapUnitGnocNimsEntity cfgMapUnitGnocNimsEntity = getEntityManager()
        .merge(cfgMapUnitGnocNimsDTO.toEntity());
    resultInSideDto.setId(cfgMapUnitGnocNimsEntity.getId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto edit(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(cfgMapUnitGnocNimsDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public CfgMapUnitGnocNimsDTO findById(Long id) {
    CfgMapUnitGnocNimsEntity cfgMapUnitGnocNimsEntity = getEntityManager()
        .find(CfgMapUnitGnocNimsEntity.class, id);
    CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO = cfgMapUnitGnocNimsEntity.toDTO();
    return cfgMapUnitGnocNimsDTO;
  }

  @Override
  public CfgMapUnitGnocNimsDTO checkCfgMapUnitGnocNimsExist(String unitNimsCode,
      String unitGnocCode, Long businessCode) {
    List<CfgMapUnitGnocNimsEntity> dataEntity = (List<CfgMapUnitGnocNimsEntity>) findByMultilParamNull(
        CfgMapUnitGnocNimsEntity.class,
        "unitNimsCode", unitNimsCode,
        "unitGnocCode", unitGnocCode,
        "businessCode", businessCode);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  public BaseDto sqlSearch(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_MAP_UNIT_GNOC_NIMS, "cfg-Map-Unit-Gnoc-Nims");
    Map<String, Object> parameters = new HashMap<>();
    if (cfgMapUnitGnocNimsDTO != null) {
      if (StringUtils.isNotNullOrEmpty(cfgMapUnitGnocNimsDTO.getSearchAll())) {
        sqlQuery += " AND (lower(m.UNIT_NIMS_CODE) LIKE :searchAll ESCAPE '\\' OR lower(m.UNIT_GNOC_CODE) LIKE :searchAll ESCAPE '\\')";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(cfgMapUnitGnocNimsDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(cfgMapUnitGnocNimsDTO.getUnitNimsCode())) {
        sqlQuery += " AND (lower(m.UNIT_NIMS_CODE) LIKE :unitNimsCode ESCAPE '\\')";
        parameters.put("unitNimsCode",
            StringUtils.convertLowerParamContains(cfgMapUnitGnocNimsDTO.getUnitNimsCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(cfgMapUnitGnocNimsDTO.getUnitGnocCode())) {
        sqlQuery += " AND (lower(m.UNIT_GNOC_CODE) LIKE :unitGnocCode ESCAPE '\\')";
        parameters.put("unitGnocCode",
            StringUtils.convertLowerParamContains(cfgMapUnitGnocNimsDTO.getUnitGnocCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(cfgMapUnitGnocNimsDTO.getBusinessCode())) {
        sqlQuery += " AND m.BUSINESS_CODE= :businessCode ";
        parameters.put("businessCode", cfgMapUnitGnocNimsDTO.getBusinessCode());
      }
      parameters.put("p_leeLocale", I18n.getLocale());
      sqlQuery += "ORDER BY m.ID DESC";
    }
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
