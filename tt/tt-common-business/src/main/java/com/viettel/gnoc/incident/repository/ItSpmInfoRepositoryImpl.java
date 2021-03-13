package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.incident.model.ItSpmInfoEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ItSpmInfoRepositoryImpl extends BaseRepository implements
    ItSpmInfoRepository {

  @Override
  public ResultInSideDto insertItSpmInfo(ItSpmInfoEntity entity) {
    return insertByModel(entity, "id");
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    ItSpmInfoEntity itSpmInfoEntity = getEntityManager().find(ItSpmInfoEntity.class, id);
    getEntityManager().remove(itSpmInfoEntity);
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteItSpmInfoByIncidentId(Long incidentId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<ItSpmInfoEntity> list = (List<ItSpmInfoEntity>) findByMultilParam(
        ItSpmInfoEntity.class, "incidentId", incidentId);
    if (list != null && list.size() > 0) {
      for (ItSpmInfoEntity entity : list) {
        getEntityManager().remove(entity);
      }
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }
}
