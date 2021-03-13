package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TroubleMopInsiteDTO;

public interface TroubleMopBusiness {

  ResultInSideDto insertTroubleMop(TroubleMopInsiteDTO troubleMopDTO);

  Datatable getListTroubleMopDTO(TroubleMopInsiteDTO troubleMopDTO);

  TroubleMopInsiteDTO findById(Long id);

  Datatable getListTroubleMopDtDTO(TroubleMopInsiteDTO troubleMopDTO);

  ResultInSideDto updateTroubleMop(TroubleMopInsiteDTO troubleMopDTO);
}
