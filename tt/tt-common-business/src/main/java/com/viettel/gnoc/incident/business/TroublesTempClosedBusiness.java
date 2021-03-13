package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TroublesTempClosedDTO;
import java.util.List;

public interface TroublesTempClosedBusiness {


  ResultInSideDto add(TroublesTempClosedDTO troublesTempClosedDTO);

  ResultInSideDto insertList(List<TroublesTempClosedDTO> troublesTempList);
}
