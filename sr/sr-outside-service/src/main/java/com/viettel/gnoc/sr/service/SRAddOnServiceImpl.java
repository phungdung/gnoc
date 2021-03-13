package com.viettel.gnoc.sr.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.sr.business.SrAddOnBusiness;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import java.util.List;
import java.util.Locale;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
@Slf4j
public class SRAddOnServiceImpl implements SRAddOnService {

  @Autowired
  protected SrAddOnBusiness srAddOnBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public List<SRCatalogDTO> getListSRCatalogByConfigGroup(String configGroup) {
    I18n.setLocaleForService(wsContext);
    return srAddOnBusiness.getListSRCatalogByConfigGroup(configGroup);
  }

  @Override
  public List<SRDTO> getListSRByConfigGroup(SRDTO dto, String configGroup) {
    I18n.setLocaleForService(wsContext);
    return srAddOnBusiness.getListSRByConfigGroup(dto, configGroup);
  }

  @Override
  public ResultDTO createSRByConfigGroup(SRDTO srInputDTO, String configGroup) {
    I18n.setLocaleForService(wsContext);
    return srAddOnBusiness.createSRByConfigGroup(srInputDTO, configGroup);
  }
}
