package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.wo.dto.WoCdGroupUnitDTO;
import com.viettel.gnoc.wo.model.WoCdGroupUnitEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoCdGroupUnitRepositoryImpl extends BaseRepository implements WoCdGroupUnitRepository {

  @Override
  public List<WoCdGroupUnitDTO> getListWoCdGroupUnitDTO(WoCdGroupUnitDTO woCdGroupUnitDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_GROUP_UNIT, "get-List-Wo-Cd-Group-Unit-DTO");
    Map<String, Object> parameters = new HashMap<>();
    if (woCdGroupUnitDTO.getCdGroupId() != null) {
      sql += " AND u.CD_GROUP_ID = :cdGroupId";
      parameters.put("cdGroupId", woCdGroupUnitDTO.getCdGroupId());
    }
    if (woCdGroupUnitDTO.getUnitId() != null) {
      sql += " AND u.UNIT_ID = :unitId";
      parameters.put("unitId", woCdGroupUnitDTO.getUnitId());
    }
    if (woCdGroupUnitDTO.getListUnitIdDel() != null && !woCdGroupUnitDTO.getListUnitIdDel()
        .isEmpty()) {
      sql += " AND u.UNIT_ID IN (:listUnitIdDel)";
      parameters.put("listUnitIdDel", woCdGroupUnitDTO.getListUnitIdDel());
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoCdGroupUnitDTO.class));
  }

  @Override
  public ResultInSideDto insertWoCdGroupUnit(WoCdGroupUnitDTO woCdGroupUnitDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().persist(woCdGroupUnitDTO.toEntity());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateWoCdGroupUnit(WoCdGroupUnitDTO woCdGroupUnitDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().merge(woCdGroupUnitDTO.toEntity());
    resultInSideDto.setId(woCdGroupUnitDTO.getCdGroupUnitId());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteWoCdGroupUnit(Long cdGroupUnitId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    WoCdGroupUnitEntity entity = getEntityManager().find(WoCdGroupUnitEntity.class, cdGroupUnitId);
    getEntityManager().remove(entity);
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteWoCdGroupUnitByCdGroupId(Long cdGroupId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    List<WoCdGroupUnitEntity> listEntity = findByMultilParam(WoCdGroupUnitEntity.class,
        "cdGroupId", cdGroupId);
    if (listEntity != null && listEntity.size() > 0) {
      for (WoCdGroupUnitEntity entity : listEntity) {
        getEntityManager().remove(entity);
      }
    }
    return resultInSideDto;
  }

  @Override
  public List<WoCdGroupUnitDTO> onSearchEntity(WoCdGroupUnitDTO woCdGroupUnitDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(WoCdGroupUnitEntity.class, woCdGroupUnitDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }
}
