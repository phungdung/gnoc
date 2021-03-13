package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.TemplateRelationsDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgTemplateRelationsRepository {

  String updateTemplateRelations(TemplateRelationsDTO dto);

  TemplateRelationsDTO findTemplateRelationsById(Long id);

  ResultInSideDto insertTemplateRelations(TemplateRelationsDTO dto);

  String deleteTemplateRelationsById(Long id);

  Datatable getListTemplateRelations(TemplateRelationsDTO templateRelationsDTO);
}
