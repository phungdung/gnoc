package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.incident.model.CfgServerNocEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgServerNocRepository {

  List<CfgServerNocEntity> getListCfgServerNocByCondition(String insertSource);

  List<CfgServerNocDTO> getListCfgServerNocByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);
}
