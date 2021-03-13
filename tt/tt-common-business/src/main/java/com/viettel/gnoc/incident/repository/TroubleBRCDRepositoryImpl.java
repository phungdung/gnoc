package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.TroublesEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class TroubleBRCDRepositoryImpl extends BaseRepository implements TroubleBRCDRepository {

  @Override
  public TroublesInSideDTO getInfoBRCDByTroubleId(Long troubleId) {
    if (troubleId > 0 && troubleId != null) {
      return getEntityManager().find(TroublesEntity.class, troubleId).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto getInsertOrUpdateInfoBRCD(TroublesInSideDTO troublesDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    TroublesEntity entity = troublesDTO.toEntity();
    if (entity.getTroubleId() != null) {
      getEntityManager().merge(entity);
    } else {
      getEntityManager().persist(entity);
    }
    resultInSideDto.setId(entity.getTroubleId());
    return resultInSideDto;
  }
}
