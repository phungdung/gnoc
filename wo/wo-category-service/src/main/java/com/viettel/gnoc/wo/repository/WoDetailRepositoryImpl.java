package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import com.viettel.gnoc.wo.model.WoDetailEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoDetailRepositoryImpl extends BaseRepository implements WoDetailRepository {

  @Override
  public ResultInSideDto insertWoDetail(WoDetailDTO woDetailDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().persist(woDetailDTO.toEntity());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto update(WoDetailDTO woDetailDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoDetailEntity woDetailEntity = getEntityManager().merge(woDetailDTO.toEntity());
    resultInSideDto.setId(woDetailEntity.getWoId());
    return resultInSideDto;
  }

  @Override
  public WoDetailDTO findById(Long woId) {
    WoDetailEntity woDetailEntity = getEntityManager().find(WoDetailEntity.class, woId);
    WoDetailDTO woDetailDTO = woDetailEntity.toDTO();
    return woDetailDTO;
  }

  @Override
  public List<WoDetailDTO> getListWoDetailDTO(WoDetailDTO woDetailDTO) {
    String sql = "SELECT d.* FROM WO_DETAIL d WHERE 1 = 1";
    Map<String, Object> parameters = new HashMap<>();
    if (woDetailDTO != null) {
      if (woDetailDTO.getWoId() != null) {
        sql += " AND d.WO_ID = :woId";
        parameters.put("woId", woDetailDTO.getWoId());
      }
      return getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoDetailDTO.class));
    }
    return null;
  }

}
