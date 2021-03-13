package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRApproveDTO;


public interface SRApproveRepository {

  ResultInSideDto insertSRApprove(SRApproveDTO srApproveDTO);

  ResultInSideDto updateSRApprove(SRApproveDTO srApproveDTO);

  SRApproveDTO findSRApproveBySrId(Long srId);
}
