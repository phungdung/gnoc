package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoTypeCfgRequiredDTO;
import com.viettel.gnoc.wo.repository.WoTypeCfgRequiredRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WoTypeCfgRequiredBusinessImpl implements WoTypeCfgRequiredBusiness {

  @Autowired
  protected WoTypeCfgRequiredRepository woTypeCfgRequiredRepository;


  @Override
  public List<WoTypeCfgRequiredDTO> findAllByWoTypeID(Long woTypeId) {
    log.debug("Request to findAllByWoTypeID : {}", woTypeId);
    return woTypeCfgRequiredRepository.findAllByWoTypeID(woTypeId);
  }


  @Override
  public ResultInSideDto add(WoTypeCfgRequiredDTO woTypeCfgRequiredDTO) {
    log.debug("Request to add : {}", woTypeCfgRequiredDTO);
    return woTypeCfgRequiredRepository.add(woTypeCfgRequiredDTO);
  }

  @Override
  public List<WoTypeCfgRequiredDTO> getListWoTypeCfgRequiredByWoTypeId(Long woTypeId,
      String cfgCode) {
    log.debug("Request to getListWoTypeCfgRequiredByWoTypeId : {}", woTypeId + " - " + cfgCode);
    return woTypeCfgRequiredRepository.getListWoTypeCfgRequiredByWoTypeId(woTypeId, cfgCode);
  }
}
