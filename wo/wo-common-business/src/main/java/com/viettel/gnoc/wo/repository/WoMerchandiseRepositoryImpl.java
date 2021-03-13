package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.wo.dto.WoMerchandiseInsideDTO;
import com.viettel.gnoc.wo.model.WoMerchandiseEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoMerchandiseRepositoryImpl extends BaseRepository implements WoMerchandiseRepository {

  @Override
  public ResultInSideDto insertWoMerchandise(WoMerchandiseInsideDTO woMerchandiseInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().persist(woMerchandiseInsideDTO.toEntity());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateListWoMerchandise(List<WoMerchandiseInsideDTO> listMerDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (listMerDTO != null && listMerDTO.size() > 0) {
      Long woId = listMerDTO.get(0).getWoId();
      List<WoMerchandiseEntity> listEntity = findByMultilParam(WoMerchandiseEntity.class,
          "woId", woId);
      if (listEntity != null && listEntity.size() > 0) {
        for (WoMerchandiseEntity entity : listEntity) {
          getEntityManager().remove(entity);
        }
      }
      for (WoMerchandiseInsideDTO merDTO : listMerDTO) {
        getEntityManager().merge(merDTO.toEntity());
      }
    }
    return resultInSideDto;
  }

  @Override
  public List<WoMerchandiseInsideDTO> getListWoMerchandiseDTO(
      WoMerchandiseInsideDTO woMerchandiseInsideDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_MERCHANDISE, "get-List-Wo-Merchandise-DTO");
    Map<String, Object> parameters = new HashMap<>();
    if (woMerchandiseInsideDTO.getWoId() != null) {
      sql += " AND m.WO_ID = :woId";
      parameters.put("woId", woMerchandiseInsideDTO.getWoId());
    }
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoMerchandiseInsideDTO.class));
  }

  @Override
  public List<WoMerchandiseInsideDTO> getListWoMerchandise(Long woId) {
    WoMerchandiseInsideDTO o = new WoMerchandiseInsideDTO();
    o.setWoId(woId);
    return onSearchEntity(WoMerchandiseEntity.class, o, 0, Integer.MAX_VALUE, "", "");
  }
}
