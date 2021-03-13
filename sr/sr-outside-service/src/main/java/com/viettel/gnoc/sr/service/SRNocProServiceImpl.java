package com.viettel.gnoc.sr.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.sr.business.SrNocProBusiness;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SRNocProServiceImpl implements SRNocProService {

  @Autowired
  protected SrNocProBusiness srNocProBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public List<SRCatalogDTO> getListServiceArraySR(String countryId) {
    I18n.setLocaleForService(wsContext);
    return srNocProBusiness.getListServiceArraySR(countryId);
  }

  @Override
  public List<SRCatalogDTO> getListServiceGroupSR(String countryId) {
    I18n.setLocaleForService(wsContext);
    return srNocProBusiness.getListServiceGroupSR(countryId);
  }

  @Override
  public List<SRCatalogDTO> getListServiceSR(String countryId) {
    I18n.setLocaleForService(wsContext);
    return srNocProBusiness.getListServiceSR(countryId);
  }

  @Override
  public List<SRCatalogDTO> getListUnitServiceSR(String countryId) {
    I18n.setLocaleForService(wsContext);
    return srNocProBusiness.getListUnitServiceSR(countryId);
  }

  @Override
  public List<SRCatalogDTO> getListRoleServiceSR(String countryId) {
    I18n.setLocaleForService(wsContext);
    return srNocProBusiness.getListRoleServiceSR(countryId);
  }

  @Override
  public ResultDTO getListStatusSR(List<String> lstSrCode, List<String> lstStatus, String fromDate,
      String toDate) {
    I18n.setLocaleForService(wsContext);
    return srNocProBusiness.getListStatusSR(lstSrCode, lstStatus, fromDate, toDate);
  }

  @Override
  public ResultDTO createSRForNoc(SRDTO srDTO) {
    I18n.setLocaleForService(wsContext);
    return srNocProBusiness.createSRForNoc(srDTO);
  }
}
