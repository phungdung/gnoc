package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailDTO;
import com.viettel.gnoc.maintenance.model.MrMaterialDisplacementEntity;
import com.viettel.gnoc.ws.dto.MrMaterialDTO;
import com.viettel.gnoc.ws.dto.MrMaterialDisplacementDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrMaterialDisplacementRepositoryImpl extends BaseRepository implements
    MrMaterialDisplacementRepository {

  @Override
  public List<MrMaterialDTO> getListMrMaterialDTO2(MrMaterialDTO mrMaterialDTO, String userManager,
      String woCode, int rowStart, int maxRow) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_MATERIAL_DISPLACEMENT,
            "getListMrMaterialDTO2");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", "en_US");
    if (StringUtils.isNotNullOrEmpty(mrMaterialDTO.getMaterialName())) {
      sql += " AND LOWER(MATERIAL_NAME) LIKE :materialName ESCAPE '\\' ";
      parameters.put("materialName",
          StringUtils.convertLowerParamContains(mrMaterialDTO.getMaterialName()));
    }
    if (StringUtils.isNotNullOrEmpty(mrMaterialDTO.getSerial())) {
      sql += " AND LOWER(SERIAL) LIKE :serial ESCAPE '\\' ";
      parameters.put("serial",
          StringUtils.convertLowerParamContains(mrMaterialDTO.getSerial()));
    }
    if (!StringUtils.isStringNullOrEmpty(mrMaterialDTO.getDateTime())) {
      sql += " AND TO_DATE(TO_CHAR(DATE_TIME, 'MM/YYYY'), 'MM/YYYY') =TO_DATE(:dateTime,'MM/YYYY')";
      parameters.put("dateTime", mrMaterialDTO.getDateTime());
    }
    if (!StringUtils.isStringNullOrEmpty(mrMaterialDTO.getMarketCode())) {
      sql += " AND MARKET_CODE = :marketCode ";
      parameters.put("marketCode", mrMaterialDTO.getMarketCode());
    }
    if (!StringUtils.isStringNullOrEmpty(userManager)) {
      MrDeviceBtsDTO mrDeviceBtsDTO = getMarketCodeByUserManager(userManager);
      if (mrDeviceBtsDTO != null) {
        sql += "  AND MARKET_CODE = :marketCodeManager ";
        parameters.put("marketCodeManager", mrDeviceBtsDTO.getMarketCode());
      }
    }

    if (!StringUtils.isStringNullOrEmpty(woCode)) {
      MrScheduleBtsHisDetailDTO mrScheduleBtsHisDetailDTO = getDeviceTypeByWo(woCode);
      if (mrScheduleBtsHisDetailDTO != null) {
        sql += "  AND DEVICE_TYPE = :deviceType ";
        parameters.put("deviceType", mrScheduleBtsHisDetailDTO.getDeviceType());
      }
    }

    sql += (" ORDER BY MATERIAL_ID DESC ");
    Query query = getEntityManager().createNativeQuery(sql);
    query.unwrap(SQLQuery.class).
        addScalar("materialId", new StringType()).
        addScalar("materialName", new StringType()).
        addScalar("serial", new StringType()).
        addScalar("unitPrice", new StringType()).
        addScalar("dateTime", new StringType()).
        addScalar("deviceType", new StringType()).
        addScalar("marketCode", new StringType()).
        addScalar("materialNameEN", new StringType()).
        setResultTransformer(Transformers.aliasToBean(MrMaterialDTO.class));
    for (Map.Entry<String, Object> item : parameters.entrySet()) {
      query.setParameter(item.getKey(), item.getValue());
    }
    query.setFirstResult(rowStart);
    query.setMaxResults(maxRow);
    return query.getResultList();
  }

  @Override
  public ResultDTO insertOrUpdateListMrMaterialDisplacement(MrMaterialDisplacementDTO dto) {
    ResultDTO resultDTO = new ResultDTO(RESULT.SUCCESS, RESULT.SUCCESS, RESULT.SUCCESS);
    MrMaterialDisplacementEntity mrMaterialDisplacementEntity = getEntityManager()
        .merge(dto.toEntity());
    resultDTO.setId(String.valueOf(mrMaterialDisplacementEntity.getMaterialId()));
    return resultDTO;
  }

  @Override
  public List<MrMaterialDisplacementDTO> getListMrMaterialDisplacementDTO2(
      MrMaterialDisplacementDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_MATERIAL_DISPLACEMENT,
            "getListMrMaterialDisplacementDTO2");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(dto.getMaterialName())) {
      sql += " AND UPPER(T1.MATERIAL_NAME) LIKE :materialName ESCAPE '\\' ";
      parameters.put("materialName", StringUtils.convertUpperParamContains(dto.getMaterialName()));
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getSerial())) {
      sql += " AND UPPER(T1.SERIAL) LIKE :serial ESCAPE '\\'";
      parameters.put("serial", StringUtils.convertUpperParamContains(dto.getSerial()));
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getUnit())) {
      sql += " AND UPPER(T1.UNIT) LIKE :unit ESCAPE '\\' ";
      parameters.put("unit", StringUtils.convertUpperParamContains(dto.getUnit()));
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getDeviceType())) {
      sql += " AND T1.DEVICE_TYPE =:deviceType ";
      parameters.put("deviceType", dto.getDeviceType());
    }
//    if (!StringUtils.isStringNullOrEmpty(dto.getMaterialId())) {
//      sql+=" AND T1.MATERIAL_ID =:materialId ";
//      parameters.put("materialId", dto.getMaterialId());
//    }
    if (dto.getListId() != null && dto.getListId().size() > 0) {
      sql += " AND T1.MATERIAL_ID NOT IN (:lstExclude) ";
      parameters.put("lstExclude", dto.getListId());
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrMaterialDisplacementDTO.class));
  }

  public MrDeviceBtsDTO getMarketCodeByUserManager(String userManager) {
    try {
      Map<String, Object> params = new HashMap<>();
      StringBuilder sql = new StringBuilder();
      MrDeviceBtsDTO result = new MrDeviceBtsDTO();
      sql.append("SELECT DISTINCT MD.MARKET_CODE marketCode FROM MR_DEVICE_BTS MD WHERE 1=1  ");
      if (!StringUtils.isStringNullOrEmpty(userManager)) {
        sql.append(" AND MD.USER_MANAGER = :userManager ");
        params.put("userManager", userManager);
      }
      List<MrDeviceBtsDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(MrDeviceBtsDTO.class));

      if (lst != null && !lst.isEmpty()) {
        result = lst.get(0);
      }
      return result;

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public MrScheduleBtsHisDetailDTO getDeviceTypeByWo(String woCode) {
    try {
      StringBuilder sql = new StringBuilder();
      Map<String, Object> params = new HashMap<>();
      MrScheduleBtsHisDetailDTO result = new MrScheduleBtsHisDetailDTO();
      sql.append(
          "SELECT DISTINCT T1.DEVICE_TYPE deviceType FROM MR_SCHEDULE_BTS_HIS_DETAIL T1 WHERE 1=1  ");
      if (!StringUtils.isStringNullOrEmpty(woCode)) {
        sql.append(" AND T1.WO_CODE = :WOCODE");
        params.put("WOCODE", woCode);
      }

      List<MrScheduleBtsHisDetailDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params,
              BeanPropertyRowMapper.newInstance(MrScheduleBtsHisDetailDTO.class));

      if (lst != null && !lst.isEmpty()) {
        result = lst.get(0);
      }
      return result;

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
