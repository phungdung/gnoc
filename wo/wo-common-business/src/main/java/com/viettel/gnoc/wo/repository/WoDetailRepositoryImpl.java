package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import com.viettel.gnoc.wo.model.WoDetailEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoDetailRepositoryImpl extends BaseRepository implements WoDetailRepository {

  @Override
  public ResultInSideDto insertUpdateWoDetail(WoDetailDTO woDetailDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().merge(woDetailDTO.toEntity());
    resultInSideDto.setId(woDetailDTO.getWoId());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public WoDetailDTO findWoDetailById(Long woId) {
    WoDetailEntity woDetailEntity = getEntityManager().find(WoDetailEntity.class, woId);
    if (woDetailEntity != null) {
      return woDetailEntity.toDTO();
    }
    return null;
  }
}
