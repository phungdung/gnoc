package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.ResultDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrProvinceMonitorRepository {

  List<ResultDTO> actionGetProvinceMonitoringParam(String unitId,
      String searchChild,
      String startDate,
      String endDate);
}
