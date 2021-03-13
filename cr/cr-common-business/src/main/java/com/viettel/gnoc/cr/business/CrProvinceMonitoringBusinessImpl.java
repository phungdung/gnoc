package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.cr.repository.CrProvinceMonitorRepository;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class CrProvinceMonitoringBusinessImpl implements CrProvinceMonitoringBusiness {

  @Autowired
  CrProvinceMonitorRepository crProvinceMonitorRepository;

  @Override
  public List<ResultDTO> actionGetProvinceMonitoringParam(String unitId,
      String searchChild,
      String startDate,
      String endDate) {
    return crProvinceMonitorRepository
        .actionGetProvinceMonitoringParam(unitId, searchChild, startDate, endDate);
  }
}
