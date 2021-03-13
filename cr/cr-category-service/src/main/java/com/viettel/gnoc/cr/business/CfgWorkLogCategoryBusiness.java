package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import java.util.List;

public interface CfgWorkLogCategoryBusiness {

  WorkLogCategoryInsideDTO findWorkLogCategoryById(Long id);

  ResultInSideDto insertWorkLogCategory(WorkLogCategoryInsideDTO workLogCategoryDTO);

  String deleteWorkLogCategoryById(Long id);

  String updateWorkLogCategory(WorkLogCategoryInsideDTO dto);

  Datatable getListWorkLogCategory(WorkLogCategoryInsideDTO dto);

  List<WorkLogCategoryInsideDTO> getListWorkLogType();
}
