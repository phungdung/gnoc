package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoDeclareServiceDTO;
import com.viettel.gnoc.wo.model.WoDeclareServiceEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoDeclareServiceRepositoryImpl extends BaseRepository implements
    WoDeclareServiceRepository {

  @Override
  public ResultInSideDto insertOrUpdateWoDeclareService(WoDeclareServiceDTO woDeclareServiceDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<WoDeclareServiceEntity> datas = findByMultilParam(WoDeclareServiceEntity.class, "woId",
        woDeclareServiceDTO.getWoId());
    if (datas != null && datas.size() > 0) {
      for (WoDeclareServiceEntity entity : datas) {
        getEntityManager().remove(entity);
      }
    }
    WoDeclareServiceEntity woEntity = getEntityManager().merge(woDeclareServiceDTO.toEntity());
    resultInSideDto.setId(woEntity.getWoId());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public WoDeclareServiceEntity findById(Long woId) {
    if (!StringUtils.isLongNullOrEmpty(woId)) {
      return getEntityManager().find(WoDeclareServiceEntity.class, woId);
    }
    return null;
  }
}
