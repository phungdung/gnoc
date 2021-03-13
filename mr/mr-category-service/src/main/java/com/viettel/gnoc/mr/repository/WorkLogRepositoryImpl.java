package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import com.viettel.gnoc.wo.model.WorkLogEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WorkLogRepositoryImpl extends BaseRepository implements WorkLogRepository {

  @Override
  public ResultInSideDto insertWorkLog(WorkLogEntity workLogEntity) {
    log.debug("Request to insertTroubles: {}", workLogEntity);
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    workLogEntity = getEntityManager().merge(workLogEntity);
    resultInSideDto.setId(workLogEntity.getWorkLogId());
    return resultInSideDto;
  }

  @Override
  public List<WorkLogInsiteDTO> getListWorkLogDTO(WorkLogInsiteDTO dto) {
    return onSearchEntity(WorkLogEntity.class, dto, dto.getPage(), dto.getPageSize(),
        dto.getSortType(), dto.getSortName());
  }
}
