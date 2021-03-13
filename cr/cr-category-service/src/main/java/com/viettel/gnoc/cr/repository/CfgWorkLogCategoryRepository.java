package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgWorkLogCategoryRepository {

  WorkLogCategoryInsideDTO findWorkLogCategoryById(Long id);

  ResultInSideDto insertWorkLogCategory(WorkLogCategoryInsideDTO workLogCategoryDTO);

  String deleteWorkLogCategoryById(Long id);

  String updateWorkLogCategory(WorkLogCategoryInsideDTO dto);

  Datatable getListWorkLogCategory(WorkLogCategoryInsideDTO dto);

  List<WorkLogCategoryInsideDTO> getListWorkLogType();
}
