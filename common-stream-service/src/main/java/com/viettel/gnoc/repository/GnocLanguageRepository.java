package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocLanguageDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import org.springframework.stereotype.Repository;

@Repository
public interface GnocLanguageRepository {

  ResultInSideDto insertGnocLanguageDTO(GnocLanguageDto gnocLanguageDto);

  String updateGnocLanguageDTO(GnocLanguageDto dto);

  String deleteGnocLanguageById(Long id);

  Datatable getListGnocLanguage(GnocLanguageDto dto);

  GnocLanguageDto findGnocLanguageId(Long id);
}
