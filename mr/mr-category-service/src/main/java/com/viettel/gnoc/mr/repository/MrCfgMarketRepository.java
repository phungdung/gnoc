package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCfgMarketDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrCfgMarketRepository {

  List<MrCfgMarketDTO> getListCfgMarket(MrCfgMarketDTO mrCfgMarketDTO);

  ResultInSideDto add(MrCfgMarketDTO mrCfgMarketDTO);

  MrCfgMarketDTO findMrCfgMarketById(Long idMarket);

  ResultInSideDto updateCreateUser(String type, MrCfgMarketDTO mrCfgMarketDTO);
}
