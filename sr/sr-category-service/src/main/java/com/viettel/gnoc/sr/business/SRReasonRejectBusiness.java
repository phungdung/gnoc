package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRReasonRejectDTO;

public interface SRReasonRejectBusiness {

  Datatable getListSRReasonRejectDTO(SRReasonRejectDTO reasonRejectDTO);

  ResultInSideDto deleteSRReasonReject(Long id);

  ResultInSideDto insertOrUpdateSRReason(SRReasonRejectDTO reasonRejectDTO);

  SRReasonRejectDTO getSRReasonRejectById(Long id);
}
