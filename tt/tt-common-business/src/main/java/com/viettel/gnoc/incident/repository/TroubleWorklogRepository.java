package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TroubleWorklogInsiteDTO;
import com.viettel.gnoc.incident.model.TroubleWorklogEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface TroubleWorklogRepository {

  ResultInSideDto insertTroubleWorklog(TroubleWorklogEntity troubleWorklogEntity);

  Datatable getListTroubleWorklogByTroubleId(TroubleWorklogInsiteDTO troubleWorklogDTO);

  ResultInSideDto deleteTroubleWorklogByTroubleId(Long troubleId);
}
