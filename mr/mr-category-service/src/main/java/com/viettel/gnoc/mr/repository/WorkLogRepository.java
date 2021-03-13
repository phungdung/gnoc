package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import com.viettel.gnoc.wo.model.WorkLogEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkLogRepository {

  ResultInSideDto insertWorkLog(WorkLogEntity workLogEntity);

  List<WorkLogInsiteDTO> getListWorkLogDTO(WorkLogInsiteDTO dto);
}
