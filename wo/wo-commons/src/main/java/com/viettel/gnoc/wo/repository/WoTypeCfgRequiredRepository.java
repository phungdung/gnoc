package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoTypeCfgRequiredDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoTypeCfgRequiredRepository {


  List<WoTypeCfgRequiredDTO> findAllByWoTypeID(Long woTypeId);

  ResultInSideDto add(WoTypeCfgRequiredDTO woTypeCfgRequiredDTO);

  ResultInSideDto delete(Long woTypeCfgRequiredId);

  List<WoTypeCfgRequiredDTO> getListWoTypeCfgRequiredByWoTypeId(Long woTypeId, String cfgCode);

}
