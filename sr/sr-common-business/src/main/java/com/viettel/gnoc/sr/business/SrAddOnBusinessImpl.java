package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.business.CfgWhiteListIpBusiness;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class SrAddOnBusinessImpl implements SrAddOnBusiness {

  @Autowired
  SrOutsideBusiness srOutsideBusiness;

  @Autowired
  CfgWhiteListIpBusiness userPassWordBusiness;

  @Override
  public ResultDTO createSRByConfigGroup(SRDTO srInputDTO, String configGroup) {
    log.info("Request to createSRByConfigGroup : {}", srInputDTO, configGroup);
    return srOutsideBusiness.createSRByConfigGroup(srInputDTO, configGroup);
  }

  @Override
  public List<SRDTO> getListSRByConfigGroup(SRDTO dto, String configGroup) {
    log.info("Request to getListSRByConfigGroup : {}", dto, configGroup);
    return srOutsideBusiness.getListSRByConfigGroup(dto, configGroup);
  }

  @Override
  public List<SRCatalogDTO> getListSRCatalogByConfigGroup(String configGroup) {
    log.info("Request to getListSRCatalogByConfigGroup : {}", configGroup);
    return srOutsideBusiness.getListSRCatalogByConfigGroup(configGroup);
  }
}
