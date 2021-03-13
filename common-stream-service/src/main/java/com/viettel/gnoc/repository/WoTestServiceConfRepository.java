package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.WoTestServiceConfDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface WoTestServiceConfRepository {

  Datatable getListWoTestServiceConf(WoTestServiceConfDTO woTestServiceConfDTO);

  ResultInSideDto add(WoTestServiceConfDTO woTestServiceConfDTO);

  ResultInSideDto edit(WoTestServiceConfDTO woTestServiceConfDTO);

  WoTestServiceConfDTO getDetail(Long id);
}
