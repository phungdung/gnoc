package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrHisDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface MrHisServiceRepository {

  ResultInSideDto insertMrHis(MrHisDTO hisDto);
}
