package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRStatusDTO;


public interface SRStatusRepository {

  Datatable getListSRStatusPage(SRStatusDTO srStatusDTO);

  ResultInSideDto add(SRStatusDTO srStatusDTO);

  ResultInSideDto edit(SRStatusDTO srStatusDTO);

  SRStatusDTO getDetail(Long configId);
}
