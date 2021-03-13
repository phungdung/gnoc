package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoTypeCfgRequiredDTO;
import java.util.List;

public interface WoTypeCfgRequiredBusiness {


  List<WoTypeCfgRequiredDTO> findAllByWoTypeID(Long woTypeId);

  ResultInSideDto add(WoTypeCfgRequiredDTO woTypeCfgRequiredDTO);

  List<WoTypeCfgRequiredDTO> getListWoTypeCfgRequiredByWoTypeId(Long woTypeId, String cfgCode);
}
