package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.incident.dto.CfgSupportFormDTO;

public interface TroublesInfoTickHelpBusiness {

  Datatable getListInfoTickHelpByWoCode(CfgSupportFormDTO dto);
}
