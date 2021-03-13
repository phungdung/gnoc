package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.MaterialThresInsideDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDTO;
import com.viettel.gnoc.wo.model.MaterialThresEntity;
import com.viettel.gnoc.wo.model.WoMaterialEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoMaterialRepositoryImpl extends BaseRepository implements WoMaterialRepository {

  private final static String DATA_EXPORT = "DATA_EXPORT";

  @Override
  public Datatable getListWoMaterialPage(MaterialThresInsideDTO materialThresDTO) {
    BaseDto baseDto = sqlSearch(materialThresDTO, null);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        materialThresDTO.getPage(), materialThresDTO.getPageSize(),
        MaterialThresInsideDTO.class,
        materialThresDTO.getSortName(), materialThresDTO.getSortType());
  }

  @Override
  public ResultInSideDto delete(Long materialThresId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MaterialThresEntity materialThresEntity = getEntityManager()
        .find(MaterialThresEntity.class, materialThresId);
    getEntityManager().remove(materialThresEntity);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertOrUpdate(MaterialThresInsideDTO materialThresDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    MaterialThresEntity materialThresEntity = materialThresDTO.toEntity();
    if (materialThresEntity.getMaterialThresId() != null) {
      getEntityManager().merge(materialThresEntity);
    } else {
      getEntityManager().persist(materialThresEntity);
    }
    resultInSideDTO.setId(materialThresEntity.getMaterialThresId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto insertOrUpdateListMaterialThres(
      List<MaterialThresInsideDTO> materialThresDTOList) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (MaterialThresInsideDTO dto : materialThresDTOList) {
      resultInSideDto = insertOrUpdate(dto);
    }
    return resultInSideDto;
  }

  @Override
  public Datatable getListDataExport(MaterialThresInsideDTO materialThresDTO) {
    BaseDto baseDto = sqlSearch(materialThresDTO, DATA_EXPORT);
    Datatable datatable = new Datatable();
    List<MaterialThresInsideDTO> materialThresDTOS = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MaterialThresInsideDTO.class));

    datatable.setData(materialThresDTOS);
    return datatable;
  }

  @Override
  public ResultInSideDto add(MaterialThresInsideDTO materialThresDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MaterialThresEntity materialThresEntity = getEntityManager().merge(materialThresDTO.toEntity());
    resultInSideDto.setId(materialThresEntity.getMaterialThresId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto edit(MaterialThresInsideDTO materialThresDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(materialThresDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public MaterialThresInsideDTO findByMaterialThresId(Long materialThresId) {
    MaterialThresEntity materialThresEntity = getEntityManager()
        .find(MaterialThresEntity.class, materialThresId);
    MaterialThresInsideDTO materialThres = materialThresEntity.toDTO();
    return materialThres;
  }

  @Override
  public WoMaterialDTO findWoMaterialById(Long materialId) {
    WoMaterialEntity woMaterialEntity = getEntityManager().find(WoMaterialEntity.class, materialId);
    if (woMaterialEntity != null) {
      return woMaterialEntity.toDTO();
    }
    return null;
  }

  @Override
  public MaterialThresInsideDTO checkMaterialThresExist(Long infraType, Long actionId,
      Long serviceId,
      Long materialId) {

    List<MaterialThresEntity> dataEntity = (List<MaterialThresEntity>) findByMultilParam(
        MaterialThresEntity.class,
        "infraType", infraType,
        "actionId", actionId,
        "serviceId", serviceId,
        "materialId", materialId);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public List<WoMaterialDTO> findAllMaterial(String materialName) {
    try {
      String sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_MATERIAL, "getAll-wo-material");
      Map<String, Object> parameters = new HashMap<>();
      if (StringUtils.isNotNullOrEmpty(materialName)) {
        sqlQuery += " AND lower(m.material_name) LIKE :materialName ESCAPE '\\' ";
        parameters
            .put("materialName", StringUtils.convertLowerParamContains(materialName));
      }
      return getNamedParameterJdbcTemplate()
          .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(WoMaterialDTO.class));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  public BaseDto sqlSearch(MaterialThresInsideDTO materialThresDTO, String export) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery;
    if (DATA_EXPORT.equals(export)) {
      sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_MATERIAL, "wo-material-export");
    } else {
      sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO_MATERIAL, "wo-material");
    }
    Map<String, Object> parameters = new HashMap<>();
    if (materialThresDTO != null) {
      if (StringUtils.isNotNullOrEmpty(materialThresDTO.getSearchAll())) {
        sqlQuery += " AND (lower(d.item_name) LIKE :searchAll ESCAPE '\\' OR lower(e.service_name) LIKE :searchAll ESCAPE '\\' OR lower(b.material_name) LIKE :searchAll ESCAPE '\\')";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(materialThresDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(materialThresDTO.getActionId())) {
        sqlQuery += " AND a.ACTION_ID= :actionId ";
        parameters.put("actionId", materialThresDTO.getActionId());
      }
      if (!StringUtils.isStringNullOrEmpty(materialThresDTO.getServiceId())) {
        sqlQuery += " AND a.SERVICE_ID= :serviceId ";
        parameters.put("serviceId", materialThresDTO.getServiceId());
      }
      if (!StringUtils.isStringNullOrEmpty(materialThresDTO.getMaterialThresId())) {
        sqlQuery += " AND a.material_thres_id= :materialThresId ";
        parameters.put("materialThresId", materialThresDTO.getMaterialThresId());
      }
      if (!StringUtils.isStringNullOrEmpty(materialThresDTO.getMaterialId())) {
        sqlQuery += " AND a.material_id= :materialId ";
        parameters.put("materialId", materialThresDTO.getMaterialId());
      }
      if (!StringUtils.isStringNullOrEmpty(materialThresDTO.getInfraType())) {
        sqlQuery += " AND llex.ITEM_VALUE= :infraType ";
        parameters.put("infraType", materialThresDTO.getInfraType().toString());
      }
      parameters.put("p_leeLocale", I18n.getLocale());
    }
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
