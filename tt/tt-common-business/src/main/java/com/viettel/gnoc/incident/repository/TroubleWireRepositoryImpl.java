package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.incident.dto.TroubleWireDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TroubleWireRepositoryImpl extends BaseRepository implements TroubleWireRepository {

  @Override
  public ResultInSideDto insertTroubleWire(TroubleWireDTO troubleWireDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().persist(troubleWireDTO.toEntity());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }
}
