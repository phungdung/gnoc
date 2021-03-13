package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrITSoftCatMarketDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrITSoftCatMarketRepository {

  Datatable getListMrCatMarketSearch(MrITSoftCatMarketDTO mrITSoftCatMarketDTO);

  List<MrITSoftCatMarketDTO> getListMrCatMarketSearchNoPage(MrITSoftCatMarketDTO mrITSoftCatMarketDTO);

  ResultInSideDto insert(MrITSoftCatMarketDTO mrITSoftCatMarketDTO);

  ResultInSideDto update(MrITSoftCatMarketDTO mrITSoftCatMarketDTO);

  ResultInSideDto delete(String marketCode);

  MrITSoftCatMarketDTO checkExist(String marketCode);
}
