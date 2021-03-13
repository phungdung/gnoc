package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.WoPendingDTO;
import com.viettel.gnoc.wo.model.WoPendingEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoPendingRepositoryImpl extends BaseRepository implements WoPendingRepository {

  @Override
  public List<WoPendingDTO> getListWoPendingByWoId(Long woId) {
    List<WoPendingEntity> listEntity = findByMultilParam(WoPendingEntity.class, "woId", woId);
    if (listEntity != null && listEntity.size() > 0) {
      List<WoPendingDTO> listDTO = new ArrayList<>();
      for (WoPendingEntity entity : listEntity) {
        listDTO.add(entity.toDTO());
      }
      return listDTO;
    }
    return null;
  }

  @Override
  public ResultInSideDto insertWoPending(WoPendingDTO woPendingDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().persist(woPendingDTO.toEntity());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateWoPending(WoPendingDTO woPendingDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().merge(woPendingDTO.toEntity());
    resultInSideDto.setId(woPendingDTO.getWoPendingId());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteWoPending(Long woPendingId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    WoPendingEntity entity = getEntityManager().find(WoPendingEntity.class, woPendingId);
    getEntityManager().remove(entity);
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }
}
