package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.WoTestServiceConfDTO;

public interface WoTestServiceConfBussiness {

  Datatable getListWoTestServiceConf(WoTestServiceConfDTO dto);

  ResultInSideDto insert(WoTestServiceConfDTO woTestServiceConfDTO);

  ResultInSideDto update(WoTestServiceConfDTO woTestServiceConfDTO);

  WoTestServiceConfDTO getDetail(Long id);
}

