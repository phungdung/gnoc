package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteInsideDTO;
import com.viettel.gnoc.wo.model.WoMaterialDeducteEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoMaterialDeducteRepositoryImpl extends BaseRepository implements
    WoMaterialDeducteRepository {

  @Override
  public List<WoMaterialDeducteInsideDTO> getListWoMaterialDeducteByWoId(Long woId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO_MATERIAL_DEDUCTE,
        "get-List-Wo-Material-Deducte-By-Wo-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("woId", woId);
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoMaterialDeducteInsideDTO.class));
  }

  @Override
  public ResultInSideDto updateWoMaterialDeducte(
      WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().merge(woMaterialDeducteInsideDTO.toEntity());
    resultInSideDto.setId(woMaterialDeducteInsideDTO.getWoMaterialDeducteId());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public List<WoMaterialDeducteInsideDTO> getMaterialDeducteKeyByWO(Long woId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO_MATERIAL_DEDUCTE,
        "get-Material-Deducte-Key-By-WO");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("woId", woId);
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoMaterialDeducteInsideDTO.class));
  }

  @Override
  public List<WoMaterialDeducteInsideDTO> getListWoMaterialDeducteDeleteByWoIdAndUserId(Long woId,
      Long userId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO_MATERIAL_DEDUCTE,
        "get-List-Wo-Material-Deducte-By-Wo-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("woId", woId);
    sql += " AND d.USER_ID = :userId";
    parameters.put("userId", userId);
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoMaterialDeducteInsideDTO.class));
  }

  @Override
  public ResultInSideDto deleteWoMaterialDeducte(Long woMaterialDeducteId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    WoMaterialDeducteEntity entity = getEntityManager()
        .find(WoMaterialDeducteEntity.class, woMaterialDeducteId);
    getEntityManager().remove(entity);
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertWoMaterialDeducte(
      WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().persist(woMaterialDeducteInsideDTO.toEntity());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public List<WoMaterialDeducteInsideDTO> onSearch(
      WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(WoMaterialDeducteEntity.class, woMaterialDeducteInsideDTO, rowStart,
        maxRow,
        sortType, sortFieldList);
  }

  @Override
  public List<WoMaterialDeducteDTO> getMaterialDeducteKeyByWOOutSide(Long woId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO_MATERIAL_DEDUCTE,
        "get-Material-Deducte-Key-By-WO");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("woId", woId);
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoMaterialDeducteDTO.class));
  }
}
