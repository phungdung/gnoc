package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.maintenance.dto.MrCrRecreatedDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrCrRecreatedRepositoryImpl extends BaseRepository implements MrCrRecreatedRepository {

  @Override
  public ResultInSideDto insert(MrCrRecreatedDTO mrCrRecreatedDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().persist(mrCrRecreatedDTO.toEntity());
    return resultInSideDto;
  }
}
