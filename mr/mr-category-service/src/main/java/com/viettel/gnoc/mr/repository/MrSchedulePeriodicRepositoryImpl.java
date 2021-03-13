package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.maintenance.dto.MrSchedulePeriodicDTO;
import com.viettel.gnoc.maintenance.model.MrSchedulePeriodicEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrSchedulePeriodicRepositoryImpl extends BaseRepository implements
    MrSchedulePeriodicRepository {

  @Override
  public List<MrSchedulePeriodicDTO> search(MrSchedulePeriodicDTO mrSchedulePeriodicDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(MrSchedulePeriodicEntity.class, mrSchedulePeriodicDTO, rowStart, maxRow,
        sortType,
        sortFieldList);
  }

  @Override
  public ResultInSideDto insertMrSchedulePeriodic(MrSchedulePeriodicDTO mrSchedulePeriodicDTO) {
    return insertByModel(mrSchedulePeriodicDTO.toEntity(), colId);
  }

  private static final String colId = "mrspId";
}
