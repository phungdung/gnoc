package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.MapProvinceCdDTO;
import com.viettel.gnoc.wo.model.MapProvinceCdEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MapProvinceCdRepositoryImpl extends BaseRepository implements MapProvinceCdRepository {

  @Override
  public ResultInSideDto add(MapProvinceCdDTO mapProvinceCdDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    MapProvinceCdEntity mapProvinceCdEntity = getEntityManager()
        .merge(mapProvinceCdDTO.toEntity());
    resultInSideDTO.setId(mapProvinceCdEntity.getId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto insertOrUpdateListImport(List<MapProvinceCdDTO> list) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    for (MapProvinceCdDTO mapProvinceCdDTO : list) {
      add(mapProvinceCdDTO);
    }
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto edit(MapProvinceCdDTO mapProvinceCdDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(mapProvinceCdDTO.toEntity());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteMapProvinceCd(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    MapProvinceCdEntity odTypeEntity = getEntityManager().find(MapProvinceCdEntity.class, id);
    getEntityManager().remove(odTypeEntity);
    return resultInSideDTO;
  }

  @Override
  public Datatable getListDTOSearchWeb(MapProvinceCdDTO mapProvinceCdDTO) {
    BaseDto baseDto = sqlGetListDTOSearchWeb(mapProvinceCdDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        mapProvinceCdDTO.getPage(), mapProvinceCdDTO.getPageSize(),
        MapProvinceCdDTO.class,
        mapProvinceCdDTO.getSortName(), mapProvinceCdDTO.getSortType());
  }

  @Override
  public MapProvinceCdDTO checkMapProvinceCdExist(String locationCode) {
    List<MapProvinceCdEntity> dataEntity = (List<MapProvinceCdEntity>) findByMultilParam(
        MapProvinceCdEntity.class,
        "locationCode", locationCode);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDto();
    }
    return null;
  }

  @Override
  public List<MapProvinceCdDTO> getListDTOSearchWebExport(MapProvinceCdDTO mapProvinceCdDTO) {
    BaseDto baseDto = sqlGetListDTOSearchWeb(mapProvinceCdDTO);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MapProvinceCdDTO.class));
  }

  @Override
  public MapProvinceCdDTO getDetail(Long id) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MAP_PROVINCE_CD,
        "get-detail_map_province");
    Map<String, Object> param = new HashMap<>();
    param.put("id", id);
    List<MapProvinceCdDTO> obj = getNamedParameterJdbcTemplate()
        .query(sql, param, BeanPropertyRowMapper.newInstance(MapProvinceCdDTO.class));
    if (obj != null && obj.size() > 0) {
      return obj.get(0);
    }
    return null;
  }

  public BaseDto sqlGetListDTOSearchWeb(MapProvinceCdDTO mapProvinceCdDTO) {
    BaseDto baseDto = new BaseDto();
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MAP_PROVINCE_CD, "get-List-DTO-Search-Web");

    if (StringUtils.isNotNullOrEmpty(mapProvinceCdDTO.getSearchAll())) {
      sqlQuery += " and (lower(a.location_name) LIKE :searchAll ESCAPE '\\' ";
      sqlQuery += " OR lower(a.location_code) LIKE :searchAll ESCAPE '\\' ";
      sqlQuery += " OR lower(c.wo_group_name) LIKE :searchAll ESCAPE '\\' ";
      sqlQuery += " OR lower(c.WO_GROUP_CODE) LIKE :searchAll ESCAPE '\\')";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(mapProvinceCdDTO.getSearchAll()));
    }

    if (mapProvinceCdDTO.getCdId() != null) {
      sqlQuery += " AND l.cd_id =:cdID ";
      parameters.put("cdID", mapProvinceCdDTO.getCdId());
    }
    if (!StringUtils.isStringNullOrEmpty(mapProvinceCdDTO.getLocationCode())) {
      sqlQuery += " AND l.location_code =:locationCode ";
      parameters.put("locationCode", mapProvinceCdDTO.getLocationCode());
    }
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }


}
