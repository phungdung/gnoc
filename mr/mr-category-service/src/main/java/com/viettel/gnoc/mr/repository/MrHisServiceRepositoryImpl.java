package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.MrHisDTO;
import com.viettel.gnoc.maintenance.model.MrHisEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrHisServiceRepositoryImpl extends BaseRepository implements MrHisServiceRepository {

  @Override
  public ResultInSideDto insertMrHis(MrHisDTO mrHisDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    MrHisEntity entity = getEntityManager().merge(mrHisDTO.toEntity());
    resultInSideDTO.setId(entity.getMhsId());
    return resultInSideDTO;
  }
}
