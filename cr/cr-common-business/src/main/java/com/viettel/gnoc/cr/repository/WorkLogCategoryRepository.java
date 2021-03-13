package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.wo.dto.UserGroupCategoryDTO;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkLogCategoryRepository {

  List<WorkLogCategoryInsideDTO> getListWorkLogCategoryDTO(
      WorkLogCategoryInsideDTO workLogCategoryDTO,
      int rowStart, int maxRow,
      String sortType, String sortFieldList);

  Datatable getListWorklogSearch(WorkLogInsiteDTO workLogInsiteDTO);

  List<WorkLogInsiteDTO> getListWorkLogDTO(WorkLogInsiteDTO dto);

  List<UserGroupCategoryDTO> getListUserGroupBySystem(UserGroupCategoryDTO lstCondition);
}
