package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.cr.business.CrDtBusiness;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.VMSAMopDetailDTO;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CrDtServiceImpl implements CrDtService {

  @Autowired
  CrDtBusiness crDtBusiness;

  @Resource
  private WebServiceContext wsContext;

  @Override
  public List<ItemDataCR> getAllActiveAffectedService(String userService, String passService) {
    I18n.setLocaleForService(wsContext);
    return crDtBusiness.getAllActiveAffectedService(userService, passService, I18n.getLocale());
  }

  @Override
  public ResultDTO insertVMSADT(String userService, String passService, String systemCode,
      Long crId, Long validateKey, int createMopSuccess, String createMopDetail,
      List<VMSAMopDetailDTO> mopDTOList, String nationCode) {
    log.debug("Request to insertVMSADT : {}", userService, passService, systemCode, crId,
        validateKey, createMopSuccess, createMopDetail, mopDTOList, nationCode);
    I18n.setLocaleForService(wsContext);
    return crDtBusiness
        .insertVMSADT(userService, passService, systemCode, crId, validateKey, createMopSuccess,
            createMopDetail,
            mopDTOList, nationCode, I18n.getLocale());
  }

}
