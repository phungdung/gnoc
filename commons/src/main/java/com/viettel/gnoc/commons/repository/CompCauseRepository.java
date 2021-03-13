package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CompCauseDTO;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CompCauseRepository {

  List<CompCauseDTO> getComCauseList(Long serviceTypeId, List<Long> ccGroupId, Long parentId,
      Integer levelId, String lineType, Long cfgType, String nationCode, Boolean isEnable)
      throws Exception;

  Map<String, Object> mapLanguageExchange(String leelocale, String mySystem, String myBussiness);

  CompCauseDTO findCompCauseById(Long id);

  CompCauseDTO getCompCauseById(Long id);
}
