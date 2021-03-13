package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CfgTimeExtendTtDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;
import org.springframework.stereotype.Repository;


@Repository
public interface CfgTimeExtendTtRepository {

  List<CfgTimeExtendTtDTO> getListCfgTimeExtendTtByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);
}
