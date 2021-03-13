package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.model.WoTestServiceMapEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.WoTestServiceMapDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class WoTestServiceMapRepositoryImpl extends BaseRepository implements
    WoTestServiceMapRepository {

  @Override
  public List<WoTestServiceMapDTO> getListWoTestServiceMapDTO(
      WoTestServiceMapDTO woTestServiceMapDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    return onSearchEntity(WoTestServiceMapEntity.class, woTestServiceMapDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public ResultDTO createObject(WoTestServiceMapDTO woTestServiceMapDTO) {
    ResultDTO resultDTO = new ResultDTO();
    resultDTO.setKey(RESULT.SUCCESS);
    resultDTO.setMessage(RESULT.SUCCESS);
    WoTestServiceMapEntity woTestServiceMapEntity = getEntityManager()
        .merge(woTestServiceMapDTO.toEntity());
    resultDTO.setId(woTestServiceMapEntity.getId() == null ? null : woTestServiceMapEntity.getId().toString());
    return resultDTO;
  }

  @Override
  public String insertList(List<WoTestServiceMapDTO> lsWoTestServiceMapDTOS) {
    for(WoTestServiceMapDTO item : lsWoTestServiceMapDTOS) {
      createObject(item);
    }
    return RESULT.SUCCESS;
  }
}
