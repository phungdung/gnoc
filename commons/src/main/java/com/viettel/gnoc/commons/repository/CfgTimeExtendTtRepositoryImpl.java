package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CfgTimeExtendTtDTO;
import com.viettel.gnoc.commons.model.CfgTimeExtendTtEntity;
import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgTimeExtendTtRepositoryImpl extends BaseRepository implements
    CfgTimeExtendTtRepository {

  @Override
  public List<CfgTimeExtendTtDTO> getListCfgTimeExtendTtByCondition(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    return onSearchByConditionBean(new CfgTimeExtendTtEntity(), lstCondition, rowStart, maxRow,
        sortType,
        sortFieldList);
  }
}
