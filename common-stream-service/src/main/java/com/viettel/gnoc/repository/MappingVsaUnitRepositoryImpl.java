package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MappingVsaUnitDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.model.MappingVsaUnitEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MappingVsaUnitRepositoryImpl extends BaseRepository implements
    MappingVsaUnitRepository {

  public String getSQL(MappingVsaUnitDTO mappingVsaUnitDTO, Map<String, Object> parameters) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_UNIT, "getLstMappingVsaUnit");

    if (StringUtils.isNotNullOrEmpty(mappingVsaUnitDTO.getSearchAll())) {
      sqlQuery += " AND lower(utApp.unit_name) LIKE :searchAll ESCAPE '\\' OR lower(utApp.unit_code) LIKE :searchAll ESCAPE '\\' ";
      parameters
          .put("searchAll",
              StringUtils.convertLowerParamContains(mappingVsaUnitDTO.getSearchAll()));
    }

    if (mappingVsaUnitDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(mappingVsaUnitDTO.getAppUnitCode())) {
        sqlQuery += " and lower(utApp.unit_code) LIKE :unit_code ESCAPE '\\' ";
        parameters
            .put("unit_code",
                StringUtils.convertLowerParamContains(mappingVsaUnitDTO.getAppUnitCode().trim()));
      }
      if (!StringUtils.isStringNullOrEmpty(mappingVsaUnitDTO.getAppUnitName())) {
        sqlQuery += " and lower(utApp.unit_name) LIKE :unit_name ESCAPE '\\' ";
        parameters
            .put("unit_name",
                StringUtils.convertLowerParamContains(mappingVsaUnitDTO.getAppUnitName().trim()));
      }
      if (!StringUtils.isStringNullOrEmpty(mappingVsaUnitDTO.getVsaUnitCode())) {
        sqlQuery += " and lower(utVsa.unit_code) LIKE :vsa_code ESCAPE '\\' ";
        parameters
            .put("vsa_code",
                StringUtils.convertLowerParamContains(mappingVsaUnitDTO.getVsaUnitCode().trim()));
      }
      if (!StringUtils.isStringNullOrEmpty(mappingVsaUnitDTO.getVsaUnitName())) {
        sqlQuery += " and lower(utVsa.unit_name) LIKE :vsa_name ESCAPE '\\' ";
        parameters
            .put("vsa_name",
                StringUtils.convertLowerParamContains(mappingVsaUnitDTO.getVsaUnitName().trim()));
      }
      if (!StringUtils.isStringNullOrEmpty(mappingVsaUnitDTO.getAppUnitNameFull())) {
        sqlQuery += " and lower(utApp.unit_name_full) LIKE :unit_name_full ESCAPE '\\' ";
        parameters
            .put("unit_name_full", StringUtils
                .convertLowerParamContains(mappingVsaUnitDTO.getAppUnitNameFull().trim()));
      }
      if (!StringUtils.isStringNullOrEmpty(mappingVsaUnitDTO.getVsaUnitNameFull())) {
        sqlQuery += " and lower(utVsa.unit_name_full) LIKE :vsa_name_full ESCAPE '\\' ";
        parameters
            .put("vsa_name_full", StringUtils
                .convertLowerParamContains(mappingVsaUnitDTO.getVsaUnitNameFull().trim()));
      }
    }

    sqlQuery += " order by utApp.unit_code ";
    return sqlQuery;
  }

  @Override
  public Datatable getListMappingVsaUnitDTO(MappingVsaUnitDTO mappingVsaUnitDTO) {

    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = getSQL(mappingVsaUnitDTO, parameters);
    return getListDataTableBySqlQuery(sqlQuery, parameters,
        mappingVsaUnitDTO.getPage(), mappingVsaUnitDTO.getPageSize(), MappingVsaUnitDTO.class,
        mappingVsaUnitDTO.getSortName(), mappingVsaUnitDTO.getSortType());
  }

  @Override
  public ResultInSideDto updateMappingVsaUnit(MappingVsaUnitDTO mappingVsaUnitDTO) {
    return insertOrUpdate(mappingVsaUnitDTO);
  }

  public ResultInSideDto insertOrUpdate(MappingVsaUnitDTO mappingVsaUnitDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    MappingVsaUnitEntity entity = getEntityManager().merge(mappingVsaUnitDTO.toEntity());
    resultDto.setId(entity.getMvutId());

    resultDto.setKey(Constants.RESULT.SUCCESS);
    return resultDto;
  }

  @Override
  public ResultInSideDto deleteMappingVsaUnit(Long mvutId) {
    return delete(mvutId);
  }

  public ResultInSideDto delete(Long mvutId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    MappingVsaUnitEntity entity = getEntityManager().find(MappingVsaUnitEntity.class, mvutId);
    if (entity != null) {
      getEntityManager().remove(entity);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListMappingVsaUnit(List<MappingVsaUnitDTO> mappingVsaUnitListDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (MappingVsaUnitDTO item : mappingVsaUnitListDTO) {
      resultInSideDto = delete(item.getMvutId());
    }
    return resultInSideDto;
  }

  @Override
  public MappingVsaUnitDTO findMappingVsaUnitById(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    MappingVsaUnitEntity entity = getEntityManager().find(MappingVsaUnitEntity.class, id);
    if (entity != null) {
      return entity.toDTO();
    }

    return null;
  }

  @Override
  public ResultInSideDto insertMappingVsaUnit(MappingVsaUnitDTO mappingVsaUnitDTO) {
    return insertOrUpdate(mappingVsaUnitDTO);
  }

  @Override
  public ResultInSideDto insertOrUpdateListMappingVsaUnit(
      List<MappingVsaUnitDTO> mappingVsaUnitDTO) {
    return null;
  }

  @Override
  public List<MappingVsaUnitDTO> getListForName(MappingVsaUnitDTO mappingVsaUnitDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = getSQL(mappingVsaUnitDTO, parameters);
    List<MappingVsaUnitDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper
            .newInstance(MappingVsaUnitDTO.class));

    return list;
  }

  @Override
  public List<MappingVsaUnitDTO> checkExistUnitAndVsaUnit(MappingVsaUnitDTO mappingVsaUnitDTO) {
    List<MappingVsaUnitDTO> list = findByMultilParam(MappingVsaUnitEntity.class, "vsaUnitId",
        mappingVsaUnitDTO.getVsaUnitId(), "appUnitId", mappingVsaUnitDTO.getAppUnitId());

    return list;
  }

  @Override
  public List<MappingVsaUnitDTO> checkExistVsaUnit(MappingVsaUnitDTO mappingVsaUnitDTO) {
    List<MappingVsaUnitDTO> list = findByMultilParam(MappingVsaUnitEntity.class, "vsaUnitId",
        mappingVsaUnitDTO.getVsaUnitId(), "appUnitId", mappingVsaUnitDTO.getAppUnitId());

    return list;
  }

  @Override
  public List<UnitDTO> checkExistUnitNotActive(UnitDTO unitDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_UNIT, "getListUnitVSACheckActive");
    if(!StringUtils.isStringNullOrEmpty(unitDTO.getUnitCode())){
      sqlQuery += " WHERE unitCode = :unitCode ";
      parameters.put("unitCode", unitDTO.getUnitCode());
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters,
            BeanPropertyRowMapper.newInstance(UnitDTO.class));
  }
}
