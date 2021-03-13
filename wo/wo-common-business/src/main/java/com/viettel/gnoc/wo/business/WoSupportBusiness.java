package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.WoSupportDTO;
import java.util.List;

public interface WoSupportBusiness {

  ResultInSideDto insertListWoSupport(List<WoSupportDTO> lstWoSupportDTO);
}
