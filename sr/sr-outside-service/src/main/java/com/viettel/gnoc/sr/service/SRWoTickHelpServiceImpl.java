package com.viettel.gnoc.sr.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.sr.business.SrWOTickHelpBusiness;
import com.viettel.gnoc.sr.dto.SRDTO;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SRWoTickHelpServiceImpl implements SRWoTickHelpService {

  @Autowired
  protected SrWOTickHelpBusiness srWOTickHelpBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public List<SRDTO> getListSRForWOTHVSmart(SRDTO srDTO, String woId) {
    I18n.setLocaleForService(wsContext);
    return srWOTickHelpBusiness.getListSRForWOTHVSmart(srDTO, woId);
  }

  @Override
  public SRDTO getDetailSRForWOTHVSmart(String srId) {
    I18n.setLocaleForService(wsContext);
    return srWOTickHelpBusiness.getDetailSRForWOTHVSmart(srId);
  }

  @Override
  public ResultDTO createSRForWOTHVSmart(List<ObjKeyValueVsmartDTO> lstObjKeyValueVsmartDTO,
      String createUser, String serviceCode) {
    I18n.setLocaleForService(wsContext);
    return srWOTickHelpBusiness
        .createSRForWOTHVSmart(lstObjKeyValueVsmartDTO, createUser, serviceCode);
  }
}
