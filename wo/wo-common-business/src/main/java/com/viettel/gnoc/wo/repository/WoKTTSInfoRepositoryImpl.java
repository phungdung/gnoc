package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.WoKTTSInfoDTO;
import com.viettel.gnoc.wo.model.WoKTTSInfoEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoKTTSInfoRepositoryImpl extends BaseRepository implements WoKTTSInfoRepository {

  @Override
  public ResultInSideDto insertWoKTTSInfo(WoKTTSInfoDTO woKTTSInfoDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().persist(woKTTSInfoDTO.toEntity());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto inserOrUpdateWoKTTSInfo(WoKTTSInfoDTO woKTTSInfoDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    Long woId = woKTTSInfoDTO.getWoId();
    List<WoKTTSInfoEntity> listEntity = findByMultilParam(WoKTTSInfoEntity.class,
        "woId", woId);
    if (listEntity != null && listEntity.size() > 0) {
      for (WoKTTSInfoEntity entity : listEntity) {
        getEntityManager().remove(entity);
      }
    }
    getEntityManager().merge(woKTTSInfoDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public List<WoKTTSInfoDTO> getListWoKTTSInfoByWoId(Long woId) {
    List<WoKTTSInfoEntity> listEntity = findByMultilParam(WoKTTSInfoEntity.class, "woId", woId);
    if (listEntity != null && listEntity.size() > 0) {
      List<WoKTTSInfoDTO> listDTO = new ArrayList<>();
      for (WoKTTSInfoEntity entity : listEntity) {
        listDTO.add(entity.toDTO());
      }
      return listDTO;
    }
    return null;
  }
}
