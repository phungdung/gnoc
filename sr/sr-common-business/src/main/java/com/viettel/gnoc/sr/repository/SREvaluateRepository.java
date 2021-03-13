package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SREvaluateDTO;
import java.util.List;


public interface SREvaluateRepository {

  ResultInSideDto insertSREvaluate(SREvaluateDTO srEvaluateDTO);

  ResultInSideDto updateSREvaluate(SREvaluateDTO srEvaluateDTO);

  List<SREvaluateDTO> findSREvaluateBySrId(Long srId);
}
