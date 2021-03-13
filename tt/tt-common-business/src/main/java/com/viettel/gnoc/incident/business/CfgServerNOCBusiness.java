package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import java.util.List;

public interface CfgServerNOCBusiness {

  List<CfgServerNocDTO> getListCfgServerNocByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);
}
