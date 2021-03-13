package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrITSoftCatMarketDTO;

public interface MrITSoftCatMarketBusiness {

  Datatable getListMrCatMarketSearch(MrITSoftCatMarketDTO mrITSoftCatMarketDTO);

  ResultInSideDto insert(MrITSoftCatMarketDTO mrITSoftCatMarketDTO);

  ResultInSideDto update(MrITSoftCatMarketDTO mrITSoftCatMarketDTO);

  ResultInSideDto delete(String marketCode);
}
