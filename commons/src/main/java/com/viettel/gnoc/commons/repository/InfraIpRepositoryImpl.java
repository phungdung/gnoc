package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.InfraIpDTO;
import com.viettel.gnoc.commons.model.InfraIpEntity;
import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class InfraIpRepositoryImpl extends BaseRepository implements
    InfraIpRepository {

  @Override
  public List<InfraIpDTO> getListInfraIpByCondition(List<ConditionBean> lstCondition, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new InfraIpEntity(), lstCondition, rowStart, maxRow, sortType,
        sortFieldList);
  }
}
