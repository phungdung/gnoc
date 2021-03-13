package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRRenewDTO;


public interface SRRenewRepository {

  ResultInSideDto insertSRRenew(SRRenewDTO srRenewDTO);

  ResultInSideDto updateSRRenew(SRRenewDTO srRenewDTO);

  SRRenewDTO findSRRenewBySrId(Long srId);
}
