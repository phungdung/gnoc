package com.viettel.gnoc.sr.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.sr.business.SrAddOnBusiness;
import com.viettel.gnoc.sr.business.SrVsmartBusiness;
import com.viettel.gnoc.sr.business.SrWOTickHelpBusiness;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserDTO;
import com.viettel.gnoc.sr.dto.UnitSRCatalogDTO;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SRVsmartServiceImpl implements SRVsmartService {

  @Autowired
  protected SrVsmartBusiness srVsmartBusiness;

  @Autowired
  protected SrWOTickHelpBusiness srWOTickHelpBusiness;

  @Autowired
  protected SrAddOnBusiness srAddOnBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public List<SRConfigDTO> getSRReviewForVSmart(String userName) {
    I18n.setLocaleForService(wsContext);
    return srVsmartBusiness.getSRReviewForVSmart(userName);
  }

  @Override
  public List<SRCatalogDTO> getListSRCatalogForVSmart(String userName, String serviceGroup) {
    I18n.setLocaleForService(wsContext);
    return srVsmartBusiness.getListSRCatalogForVSmart(userName, serviceGroup);
  }

  @Override
  public List<SRRoleUserDTO> getListSRUserForVSmart(String serviceCode, Long unitId,
      String roleCode, String country) {
    I18n.setLocaleForService(wsContext);
    return srVsmartBusiness.getListSRUserForVSmart(serviceCode, unitId, roleCode, country);
  }

  @Override
  public List<SRDTO> getListSRForVSmart(SRDTO dto) {
    I18n.setLocaleForService(wsContext);
    return srVsmartBusiness.getListSRForVSmart(dto);
  }

  @Override
  public List<SRConfigDTO> getListSRStatusForVSmart(String userName) {
    I18n.setLocaleForService(wsContext);
    return srVsmartBusiness.getListSRStatusForVSmart(userName);
  }

  @Override
  public ResultDTO updateSRForVSmart(SRDTO srInputDTO) {
    I18n.setLocaleForService(wsContext);
    return srVsmartBusiness.updateSRForVSmart(srInputDTO);
  }

  @Override
  public SRDTO getDetailSRForVSmart(String srId, String loginUser) {
    I18n.setLocaleForService(wsContext);
    return srVsmartBusiness.getDetailSRForVSmart(srId, loginUser);
  }

  @Override
  public List<SRRoleUserDTO> getListRoleUserForVsmart(SRRoleUserDTO srRoleUserDTO) {
    I18n.setLocaleForService(wsContext);
    return srVsmartBusiness.getListRoleUserForVsmart(srRoleUserDTO);
  }

  @Override
  public List<UnitSRCatalogDTO> getListUnitSRCatalogForVsmart(SRCatalogDTO dto) {
    I18n.setLocaleForService(wsContext);
    return srVsmartBusiness.getListUnitSRCatalogForVsmart(dto);
  }

  @Override
  public List<SRConfigDTO> getListServiceGrouprForVsmart() {
    I18n.setLocaleForService(wsContext);
    return srVsmartBusiness.getListServiceGrouprForVsmart();
  }

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

  @Override
  public List<SRCatalogDTO> getListSRCatalogByConfigGroup(String configGroup) {
    I18n.setLocaleForService(wsContext);
    return srAddOnBusiness.getListSRCatalogByConfigGroup(configGroup);
  }
}
