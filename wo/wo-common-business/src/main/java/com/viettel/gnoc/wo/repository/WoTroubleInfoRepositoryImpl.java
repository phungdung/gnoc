package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.dto.WoTroubleInfoDTO;
import com.viettel.gnoc.wo.model.WoTroubleInfoEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoTroubleInfoRepositoryImpl extends BaseRepository implements WoTroubleInfoRepository {

  @Override
  public WoTroubleInfoDTO getWoTroubleInfoByWoId(Long woId) {
    List<WoTroubleInfoEntity> listEntity = findByMultilParam(WoTroubleInfoEntity.class, "woId",
        woId);
    if (listEntity != null && listEntity.size() > 0) {
      return listEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertWoTroubleInfo(WoTroubleInfoDTO woTroubleInfoDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    WoTroubleInfoEntity entity = woTroubleInfoDTO.toEntity();
    if (entity.getId() != null) {
      getEntityManager().merge(entity);
    } else {
      getEntityManager().persist(entity);
    }
    resultInSideDTO.setId(entity.getId());
    return resultInSideDTO;
  }

  @Override
  public List<WoTroubleInfoDTO> getListWoTroubleInfoByWoId(Long woId) {
    List<WoTroubleInfoEntity> listEntity = findByMultilParam(WoTroubleInfoEntity.class, "woId",
        woId);
    if (listEntity != null && listEntity.size() > 0) {
      List<WoTroubleInfoDTO> listDTO = new ArrayList<>();
      for (WoTroubleInfoEntity entity : listEntity) {
        listDTO.add(entity.toDTO());
      }
      return listDTO;
    }
    return null;
  }
}
