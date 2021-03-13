package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.WorkTimeDTO;
import com.viettel.gnoc.wo.model.WorkTimeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoTimeRepositoryImpl extends BaseRepository implements WoTimeRepository {

  @Override
  public ResultDTO insertOrUpdateWorkTime(WorkTimeDTO workTimeDTO) {
    WorkTimeEntity workTimeEntity = workTimeDTO.toEntity();
    getEntityManager().merge(workTimeEntity);
    return new ResultDTO(String.valueOf(workTimeEntity.getId()), RESULT.SUCCESS, RESULT.SUCCESS);
  }
}
