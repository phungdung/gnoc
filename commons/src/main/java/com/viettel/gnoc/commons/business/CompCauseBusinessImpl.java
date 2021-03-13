package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CompCauseDTO;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.repository.CompCauseRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author TungPV
 */
@Service
@Transactional
@Slf4j
public class CompCauseBusinessImpl implements CompCauseBusiness {

  @Autowired
  CompCauseRepository compCauseRepository;

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  @Override
  public List<CompCauseDTO> translateList(List<CompCauseDTO> lst, String locale) {
    String mySystem = Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON;
    String myBussiness = Constants.COMMON_TRANSLATE_BUSINESS.COMP_CAUSE.toString();
    Map<String, Object> mapTranslate = compCauseRepository
        .mapLanguageExchange(I18n.getLocale(), mySystem, myBussiness);
    if (mapTranslate == null || mapTranslate.isEmpty()) {
      return lst;
    }
    String sqlLanguage = (String) mapTranslate.get("sql");
    Map mapParam = (Map) mapTranslate.get("mapParam");
    Map mapType = (Map) mapTranslate.get("mapType");

    List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
        .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);

    Map<String, String> mapLan = new HashMap<String, String>();
    for (LanguageExchangeDTO dto : lstLanguage) {
      mapLan.put(dto.getBussinessId().toString(), dto.getLeeValue());
    }

    for (CompCauseDTO compCauseDTO : lst) {
      if (mapLan.get(compCauseDTO.getCompCauseId()) != null) {
        compCauseDTO.setName(mapLan.get(compCauseDTO.getCompCauseId()).trim());
      }
    }
    return lst;
  }

  @Override
  public List<CompCauseDTO> getComCauseList(Long serviceTypeId, List<Long> ccGroupId, Long parentId,
      Integer levelId, String lineType, Long cfgType, String nationCode, Boolean isEnable)
      throws Exception {
    return compCauseRepository
        .getComCauseList(serviceTypeId, ccGroupId, parentId, levelId, lineType, cfgType, nationCode,
            isEnable);
  }

  @Override
  public CompCauseDTO findCompCauseById(Long id) {
    return compCauseRepository.findCompCauseById(id);
  }

  @Override
  public List<CompCauseDTO> getCompCause(Long comcauId) {
    List<CompCauseDTO> list = new ArrayList<>();
    CompCauseDTO dto1 = getCompCauseById(comcauId);
    if (dto1 != null && dto1.getParentId() != null) {
      list.add(dto1);
      CompCauseDTO dto2 = getCompCauseById(Long.valueOf(dto1.getParentId()));
      if (dto2 != null && dto2.getParentId() != null) {
        list.add(dto2);
        CompCauseDTO dto3 = getCompCauseById(Long.valueOf(dto2.getParentId()));
        if (dto3 != null) {
          list.add(dto3);
        }
      }
    }
    return list;
  }

  @Override
  public CompCauseDTO getCompCauseById(Long comcauId) {
    return compCauseRepository.getCompCauseById(comcauId);
  }
}
