package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocLanguageDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface LanguageExchangeBussiness {

  Datatable getListLanguageExchange(LanguageExchangeDTO dto);

  List<CatItemDTO> getListTableBySystem(String systemName);

  List<GnocLanguageDto> getListLanguage();

  LanguageExchangeDTO getDetailLanguageExchange(Long langExchangeId);

  ResultInSideDto updateLanguageExchange(LanguageExchangeDTO dto);

  ResultInSideDto insertLanguageExchange(LanguageExchangeDTO dto);

  File exportLanguageExchange(LanguageExchangeDTO languageExchangeDTO) throws Exception;

  File exportLanguageExchangeNotConfig(LanguageExchangeDTO languageExchangeDTO) throws Exception;

  ResultInSideDto importData(MultipartFile uploadFile) throws Exception;

  File getTemplate() throws Exception;

  ResultInSideDto deleteLanguageExchangeById(Long id);
}
