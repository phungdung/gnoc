package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WorkTimeDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface WoTimeRepository {

  ResultDTO insertOrUpdateWorkTime(WorkTimeDTO workTimeDTO);
}
