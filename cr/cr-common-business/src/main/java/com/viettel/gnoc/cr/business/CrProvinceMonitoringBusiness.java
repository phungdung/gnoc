package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import java.util.List;

public interface CrProvinceMonitoringBusiness {

  List<ResultDTO> actionGetProvinceMonitoringParam(String unitId,
      String searchChild,
      String startDate,
      String endDate);
}
