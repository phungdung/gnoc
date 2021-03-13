package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocLanguageDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@Repository
public interface LanguageExchangeRepository {

  List<LanguageExchangeDTO> findBySql(String sql, Map<String, String> mapParam,
      Map<String, String> mapType, Class type);

  Map<String, Object> mapLanguageExchange(String leelocale, String mySystem, String myBussiness);

  List<LanguageExchangeDTO> getListLanguageExchangeById(String system, String bussiness,
      Long bussinessId, String leeLocale);

  ResultInSideDto saveListLanguageExchange(String system, String bussiness, Long bussinessId,
      List<LanguageExchangeDTO> languageExchangeDTOS);

  ResultInSideDto saveListLanguageExchangeImport(List<LanguageExchangeDTO> languageExchangeDTOS);

  ResultInSideDto deleteLanguageExchangeById(Long id);

  ResultInSideDto deleteListLanguageExchange(String system, String bussiness, Long bussinessId);

  Datatable getListLanguageExchange(LanguageExchangeDTO dto);

  List<LanguageExchangeDTO> getDataExport(LanguageExchangeDTO dto);

  List<CatItemDTO> getListTableBySystem(String systemName);

  List<GnocLanguageDto> getListLanguage();

  LanguageExchangeDTO getDetailLanguageExchange(Long langExchangeId);

  ResultInSideDto updateLanguageExchange(LanguageExchangeDTO dto);

  ResultInSideDto insertLanguageExchange(LanguageExchangeDTO dto);

  LanguageExchangeDTO checkLanguageExchangeExist(Long appliedSystem, Long appliedBussiness,
      Long bussinessId,
      String leeLocale);

  List<LanguageExchangeDTO> getListExchangeNotConfig(LanguageExchangeDTO languageExchangeDTO);

  Map<String, String> translateMe(String myLanguage, String mySystem, String myBussiness,
      String myOption);
}
