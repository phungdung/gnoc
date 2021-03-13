package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.incident.model.CfgServerNocEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgServerNocRepositoryImpl extends BaseRepository implements
    CfgServerNocRepository {

  @Override
  public List<CfgServerNocEntity> getListCfgServerNocByCondition(String insertSource) {
    return findByMultilParam(CfgServerNocEntity.class, "insertSource", insertSource);
  }

  @Override
  public List<CfgServerNocDTO> getListCfgServerNocByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new CfgServerNocEntity(), lstCondition, rowStart, maxRow,
        sortType, sortFieldList);
  }
}
