package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.od.dto.OdTypeMapLocationDTO;
import com.viettel.gnoc.od.model.OdTypeMapLocationEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class OdTypeMapLocationRepositoryImpl extends BaseRepository implements
    OdTypeMapLocationRepository {

  @Override
  public List<OdTypeMapLocationDTO> getListOdTypeMapLocationByOdTypeId(Long odTypeId) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_ODTYPE, "get-list-od-type-map-location");
    Map<String, Object> parameters = new HashMap<>();
    if (odTypeId != null) {
      sql += " AND otml.OD_TYPE_ID = :odTypeId";
      parameters.put("odTypeId", odTypeId);
    } else {
      return null;
    }
    sql += " order by otml.ID";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(OdTypeMapLocationDTO.class));
  }

  @Override
  public ResultInSideDto add(OdTypeMapLocationDTO odTypeMapLocationDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().persist(odTypeMapLocationDTO.toEntity());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteListOdTypeMapLocationDetail(Long odTypeId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<OdTypeMapLocationEntity> listOdTypeMapLocationEntity = findByMultilParam(
        OdTypeMapLocationEntity.class,
        "odTypeId", odTypeId);
    if (listOdTypeMapLocationEntity != null && listOdTypeMapLocationEntity.size() > 0) {
      for (OdTypeMapLocationEntity odTypeMapLocationEntity : listOdTypeMapLocationEntity) {
        getEntityManager().remove(odTypeMapLocationEntity);
      }
    }
    return resultInSideDto;
  }

  @Override
  public OdTypeMapLocationDTO getListOdTypeMapByOdTypeIdAndLocation(Long odTypeId,
      String locationCode) {
    List<OdTypeMapLocationEntity> dataEntity = (List<OdTypeMapLocationEntity>) findByMultilParam(
        OdTypeMapLocationEntity.class,
        "odTypeId", odTypeId, "locationCode", locationCode);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }
}
