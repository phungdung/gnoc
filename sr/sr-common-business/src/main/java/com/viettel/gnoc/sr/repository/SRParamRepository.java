package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRParamDTO;
import com.viettel.gnoc.sr.model.SRParamEntity;
import java.util.List;


public interface SRParamRepository {

  ResultInSideDto insertListSRParam(List<SRParamDTO> lsSrParam);

  List<SRParamEntity> findListSRParamBySrId(Long srId);
}
