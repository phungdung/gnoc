package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoUpdateServiceInfraDTO;

public interface WoUpdateServiceInfraBusiness {

  Datatable getListWoUpdateServiceInfraPage(WoUpdateServiceInfraDTO woUpdateServiceInfraDTO);

  ResultInSideDto update(WoUpdateServiceInfraDTO woUpdateServiceInfraDTO);

}
