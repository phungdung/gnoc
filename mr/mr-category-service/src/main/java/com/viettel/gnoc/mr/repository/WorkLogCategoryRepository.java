package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkLogCategoryRepository {

  List<WorkLogCategoryInsideDTO> getListWorkLogCategoryDTO(
      WorkLogCategoryInsideDTO workLogCategoryDTO,
      int rowStart, int maxRow,
      String sortType, String sortFieldList);
}
