package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.TemplateRelationsDTO;

public interface CfgTemplateRelationsBusiness {

  String updateTemplateRelations(TemplateRelationsDTO dto);

  TemplateRelationsDTO findTemplateRelationsById(Long id);

  ResultInSideDto insertTemplateRelations(TemplateRelationsDTO templateRelationsDTO);

  String deleteTemplateRelationsById(Long id);

  Datatable getListTemplateRelations(TemplateRelationsDTO templateRelationsDTO);
}
