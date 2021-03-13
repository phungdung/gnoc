package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.dto.WoMopInfoDTO;
import com.viettel.gnoc.wo.model.WoMopInfoEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoMopInfoRepositoryImpl extends BaseRepository implements WoMopInfoRepository {

  @Override
  public ResultInSideDto saveOrUpdate(WoMopInfoDTO woMopInfoDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    if (woMopInfoDTO.getWoMopId() != null) {
      getEntityManager().merge(woMopInfoDTO.toEntity());
    } else {
      WoMopInfoEntity woMopInfoEntity = getEntityManager().merge(woMopInfoDTO.toEntity());
      resultInSideDTO.setId(woMopInfoEntity.getWoMopId());
    }
    return resultInSideDTO;
  }

  @Override
  public List<WoMopInfoEntity> getWoMopInfoByWoIdAndMopId(WoMopInfoDTO woMopInfoDTO) {
    List<WoMopInfoEntity> datas = findByMultilParam(WoMopInfoEntity.class, "woId",
        woMopInfoDTO.getWoId(),
        "mopId", woMopInfoDTO.getMopId());
    if (datas != null && datas.size() > 0) {
      return datas;
    }
    return null;
  }

}
