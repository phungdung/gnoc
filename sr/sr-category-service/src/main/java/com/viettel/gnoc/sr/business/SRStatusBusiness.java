package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRStatusDTO;

public interface SRStatusBusiness {

  Datatable getListSRStatusPage(SRStatusDTO srStatusDTO);

  ResultInSideDto insert(SRStatusDTO srStatusDTO);

  ResultInSideDto update(SRStatusDTO srStatusDTO);

  SRStatusDTO getDetail(Long configId);
}
