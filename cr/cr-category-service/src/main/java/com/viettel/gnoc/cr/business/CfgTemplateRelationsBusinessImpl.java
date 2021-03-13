package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.TemplateRelationsDTO;
import com.viettel.gnoc.cr.repository.CfgTemplateRelationsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class CfgTemplateRelationsBusinessImpl implements CfgTemplateRelationsBusiness {

  @Autowired
  private CfgTemplateRelationsRepository cfgTemplateRelationsRepository;

  @Override
  public String updateTemplateRelations(TemplateRelationsDTO dto) {
    log.debug("Request to updateTemplateRelations : {}", dto);
    return cfgTemplateRelationsRepository.updateTemplateRelations(dto);
  }

  @Override
  public TemplateRelationsDTO findTemplateRelationsById(Long id) {
    log.debug("Request to findTemplateRelationsById : {}", id);
    return cfgTemplateRelationsRepository.findTemplateRelationsById(id);
  }

  @Override
  public ResultInSideDto insertTemplateRelations(TemplateRelationsDTO dto) {
    log.debug("Request to insertTemplateRelations : {}", dto);
    return cfgTemplateRelationsRepository.insertTemplateRelations(dto);
  }

  @Override
  public String deleteTemplateRelationsById(Long id) {
    log.debug("Request to deleteTemplateRelationsById : {}", id);
    return cfgTemplateRelationsRepository.deleteTemplateRelationsById(id);
  }

  @Override
  public Datatable getListTemplateRelations(TemplateRelationsDTO templateRelationsDTO) {
    log.debug("Request to getListTemplateRelations : {}", templateRelationsDTO);
    return cfgTemplateRelationsRepository.getListTemplateRelations(templateRelationsDTO);
  }
}
