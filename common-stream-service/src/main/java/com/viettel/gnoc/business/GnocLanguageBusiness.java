package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocLanguageDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;

public interface GnocLanguageBusiness {

  ResultInSideDto insertGnocLanguageDTO(GnocLanguageDto gnocLanguageDto);

  String updateGnocLanguageDTO(GnocLanguageDto dto);

  String deleteGnocLanguageById(Long id);

  Datatable getListGnocLanguage(GnocLanguageDto dto);

  GnocLanguageDto findGnocLanguageId(Long id);
}
