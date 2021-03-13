package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCrRecreatedDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface MrCrRecreatedRepository {

  ResultInSideDto insert(MrCrRecreatedDTO mrCrRecreatedDTO);
}
