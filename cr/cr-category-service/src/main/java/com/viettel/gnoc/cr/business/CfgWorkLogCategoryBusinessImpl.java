package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.cr.repository.CfgWorkLogCategoryRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class CfgWorkLogCategoryBusinessImpl implements CfgWorkLogCategoryBusiness {

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  private CfgWorkLogCategoryRepository cfgWorkLogCategoryRepository;

  @Override
  public WorkLogCategoryInsideDTO findWorkLogCategoryById(Long id) {
    log.debug("Request to findWorkLogCategoryById : {}", id);
    WorkLogCategoryInsideDTO workLogCategoryDTO = cfgWorkLogCategoryRepository
        .findWorkLogCategoryById(id);
    workLogCategoryDTO.setListWorkLogCategory(languageExchangeRepository
        .getListLanguageExchangeById("OPEN_PM", "OPEN_PM.WORK_LOG_CATEGORY", id, null));
    return workLogCategoryDTO;
  }

  @Override
  public ResultInSideDto insertWorkLogCategory(WorkLogCategoryInsideDTO workLogCategoryDTO) {
    log.debug("Request to findWorkLogCategoryById : {}", workLogCategoryDTO);
    ResultInSideDto resultInSideDto = cfgWorkLogCategoryRepository
        .insertWorkLogCategory(workLogCategoryDTO);
    languageExchangeRepository
        .saveListLanguageExchange("OPEN_PM", "OPEN_PM.WORK_LOG_CATEGORY", resultInSideDto.getId(),
            workLogCategoryDTO.getListWorkLogCategory());
    return resultInSideDto;
  }

  @Override
  public String deleteWorkLogCategoryById(Long id) {
    log.debug("Request to findWorkLogCategoryById : {}", id);
    languageExchangeRepository
        .deleteListLanguageExchange("OPEN_PM", "OPEN_PM.WORK_LOG_CATEGORY", id);
    return cfgWorkLogCategoryRepository.deleteWorkLogCategoryById(id);
  }

  @Override
  public String updateWorkLogCategory(WorkLogCategoryInsideDTO dto) {
    log.debug("Request to updateWorkLogCategory : {}", dto);
    String result = cfgWorkLogCategoryRepository.updateWorkLogCategory(dto);
    languageExchangeRepository
        .saveListLanguageExchange("OPEN_PM", "OPEN_PM.WORK_LOG_CATEGORY", dto.getWlayId(),
            dto.getListWorkLogCategory());
    return result;
  }

  @Override
  public Datatable getListWorkLogCategory(WorkLogCategoryInsideDTO dto) {
    log.debug("Request to getListWorkLogCategory : {}", dto);
    return cfgWorkLogCategoryRepository.getListWorkLogCategory(dto);
  }

  @Override
  public List<WorkLogCategoryInsideDTO> getListWorkLogType() {
    log.debug("Request to getListWorkLogType : {}");
    return cfgWorkLogCategoryRepository.getListWorkLogType();
  }
}
