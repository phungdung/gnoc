package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.CompCauseDTO;
import java.util.List;

public interface CompCauseBusiness {

  List<CompCauseDTO> translateList(List<CompCauseDTO> lst, String locale);

  List<CompCauseDTO> getComCauseList(Long serviceTypeId, List<Long> ccGroupId, Long parentId,
      Integer levelId, String lineType, Long cfgType, String nationCode, Boolean isEnable)
      throws Exception;

  CompCauseDTO findCompCauseById(Long id);

  List<CompCauseDTO> getCompCause(Long comcauId);

  CompCauseDTO getCompCauseById(Long comcauId);
}
