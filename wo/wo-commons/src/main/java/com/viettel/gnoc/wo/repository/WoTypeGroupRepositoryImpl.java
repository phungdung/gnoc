package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.wo.dto.WoTypeGroupDTO;
import com.viettel.gnoc.wo.model.WoTypeGroupEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoTypeGroupRepositoryImpl extends BaseRepository implements WoTypeGroupRepository {

  @Override
  public List<WoTypeGroupDTO> getListWoTypeGroupDTO(WoTypeGroupDTO woTypeGroupDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE_GROUP, "get-List-Wo-Type-Group-DTO");
    Map<String, Object> parameters = new HashMap<>();
    if (woTypeGroupDTO.getWoGroupId() != null) {
      sql += " AND g.WO_GROUP_ID = :woGroupId";
      parameters.put("woGroupId", woTypeGroupDTO.getWoGroupId());
    }
    if (woTypeGroupDTO.getWoTypeId() != null) {
      sql += " AND g.WO_TYPE_ID = :woTypeId";
      parameters.put("woTypeId", woTypeGroupDTO.getWoTypeId());
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoTypeGroupDTO.class));
  }

  @Override
  public ResultInSideDto insertWoTypeGroup(WoTypeGroupDTO woTypeGroupDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().persist(woTypeGroupDTO.toEntity());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteWoTypeGroup(Long woTypeGroupId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    WoTypeGroupEntity entity = getEntityManager().find(WoTypeGroupEntity.class, woTypeGroupId);
    getEntityManager().remove(entity);
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteWoTypeGroupByWoGroupId(Long woGroupId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    List<WoTypeGroupEntity> listEntity = findByMultilParam(WoTypeGroupEntity.class,
        "woGroupId", woGroupId);
    if (listEntity != null && listEntity.size() > 0) {
      for (WoTypeGroupEntity entity : listEntity) {
        getEntityManager().remove(entity);
      }
    }
    return resultInSideDto;
  }

  @Override
  public WoTypeGroupDTO checkTypeGroupExitBy2Id(Long woGroupId, Long woTypeId) {
    List<WoTypeGroupEntity> dataEntity = (List<WoTypeGroupEntity>) findByMultilParam(
        WoTypeGroupEntity.class, "woGroupId", woGroupId, "woTypeId", woTypeId);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto updateWoTypeGroup(WoTypeGroupDTO woTypeGroupDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().merge(woTypeGroupDTO.toEntity());
    resultInSideDto.setId(woTypeGroupDTO.getWoTypeGroupId());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }
}
