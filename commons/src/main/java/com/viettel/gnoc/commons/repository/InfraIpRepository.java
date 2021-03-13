package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.InfraIpDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface InfraIpRepository {

  List<InfraIpDTO> getListInfraIpByCondition(List<ConditionBean> lstCondition, int rowStart,
      int maxRow, String sortType, String sortFieldList);
}
