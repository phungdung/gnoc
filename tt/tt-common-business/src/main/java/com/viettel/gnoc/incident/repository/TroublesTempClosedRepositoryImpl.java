package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.incident.dto.TroublesTempClosedDTO;
import com.viettel.gnoc.incident.model.TroublesTempClosedEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author ITSOL
 */

@Repository
@Slf4j
public class TroublesTempClosedRepositoryImpl extends BaseRepository implements
    TroublesTempClosedRepository {

  @Override
  public ResultInSideDto add(TroublesTempClosedDTO troublesTempClosedDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    TroublesTempClosedEntity troubleEntity = getEntityManager()
        .merge(troublesTempClosedDTO.toEntity());
    resultInSideDTO.setId(troubleEntity.getTroublesTempClosedId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto insertList(List<TroublesTempClosedDTO> troublesTempList) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    for (TroublesTempClosedDTO troublesTempClosedDTO : troublesTempList) {
      resultInSideDTO = add(troublesTempClosedDTO);
    }
    return resultInSideDTO;
  }

}
