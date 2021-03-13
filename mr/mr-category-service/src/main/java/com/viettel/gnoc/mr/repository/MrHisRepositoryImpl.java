package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.maintenance.dto.MrHisDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrHisRepositoryImpl extends BaseRepository implements MrHisRepository {

  @Override
  public ResultInSideDto insertMrHis(MrHisDTO mrHisDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().persist(mrHisDTO.toEntity());
    return resultInSideDto;
  }
}
