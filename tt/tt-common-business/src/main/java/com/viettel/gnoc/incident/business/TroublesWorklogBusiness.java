package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TroubleWorklogInsiteDTO;
import com.viettel.gnoc.incident.model.TroubleWorklogEntity;

public interface TroublesWorklogBusiness {

  ResultInSideDto insertTroubleWorklog(TroubleWorklogEntity entity);

  Datatable getListTroubleWorklogByTroubleId(TroubleWorklogInsiteDTO troubleWorklogDTO);
}
