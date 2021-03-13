package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCfgMarketDTO;
import java.util.List;

public interface MrCfgMarketBusiness {

  List<MrCfgMarketDTO> getListCfgMarket(MrCfgMarketDTO mrCfgMarketDTO);

  ResultInSideDto updateListMarket(MrCfgMarketDTO mrCfgMarketDTO);

  ResultInSideDto updateListMarketSynItSoft(MrCfgMarketDTO mrCfgMarketDTO);

  ResultInSideDto updateListMarketSynItHard(MrCfgMarketDTO mrCfgMarketDTO);
}
