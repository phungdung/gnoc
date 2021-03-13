package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.incident.repository.CfgServerNocRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CfgServerNOCBusinessImpl implements CfgServerNOCBusiness {

  @Autowired
  CfgServerNocRepository cfgServerNocRepository;

  @Override
  public List<CfgServerNocDTO> getListCfgServerNocByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return cfgServerNocRepository
        .getListCfgServerNocByCondition(lstCondition, rowStart, maxRow, sortType, sortFieldList);
  }
}
