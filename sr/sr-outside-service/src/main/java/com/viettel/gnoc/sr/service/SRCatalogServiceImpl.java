package com.viettel.gnoc.sr.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.sr.business.SrCatalogBusiness;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.repository.SRApproveRepository;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SRCatalogServiceImpl implements SRCatalogService {

  @Autowired
  SrCatalogBusiness srCatalogBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public SRCatalogDTO getDetailSRCatalog(String serviceId) {
    I18n.setLocaleForService(wsContext);
    return srCatalogBusiness.findByIdCatalog(serviceId);
  }

  @Override
  public List<SRCatalogDTO> getListCatalog(SRCatalogDTO srCatalogDTO) {
    I18n.setLocaleForService(wsContext);
    return srCatalogBusiness.getListCatalog(srCatalogDTO);
  }
}
