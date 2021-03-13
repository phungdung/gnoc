package com.viettel.gnoc.sr.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.sr.business.SrBusiness;
import com.viettel.gnoc.sr.business.SrOutsideBusiness;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRCreatedFromOtherSysDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SRWorkLogDTO;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SROutSideServiceImpl implements SROutSideService {

  @Autowired
  protected SrOutsideBusiness srOutsideBusiness;

  @Autowired
  protected SrBusiness srBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public ResultDTO putResultFromVipa(String srId, String result, String fileContentError) {
    I18n.setLocaleForService(wsContext);
    return srOutsideBusiness.putResultFromVipa(srId, result, fileContentError);
  }

  @Override
  public ResultDTO createSRFromOtherSys(SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO) {
    I18n.setLocaleForService(wsContext);
    return srOutsideBusiness.createSRFromOtherSys(srCreatedFromOtherSysDTO);
  }

  @Override
  public ResultDTO createSRByConfigGroup(SRDTO srInputDTO, String configGroup) {
    I18n.setLocaleForService(wsContext);
    return srOutsideBusiness.createSRByConfigGroup(srInputDTO, configGroup);
  }

  @Override
  public List<SRDTO> getListSRForLinkCR(String loginUser, String srCode) {
    I18n.setLocaleForService(wsContext);
    return srOutsideBusiness.getListSRForLinkCR(loginUser, srCode);
  }

  @Override
  public List<SRDTO> getListSR(SRDTO dto, int rowStart, int maxRow) {
    I18n.setLocaleForService(wsContext);
    return srOutsideBusiness.getListSR(dto, rowStart, maxRow);
  }

  @Override
  public String deleteSR(Long srId) {
    return srOutsideBusiness.deleteSRForOutside(srId);
  }

  @Override
  public List<SRDTO> getCrNumberCreatedFromSR(SRDTO dto, int rowStart, int maxRow) {
    I18n.setLocaleForService(wsContext);
    List<SRDTO> lstResult = srOutsideBusiness
        .getCrNumberCreatedFromSR(dto, rowStart, maxRow);
    return lstResult;
  }

  @Override
  public SRDTO getDetailSR(String srId, Long userId) {
    I18n.setLocaleForService(wsContext);
    return srOutsideBusiness.getDetailSR(srId, userId);
  }

  @Override
  public ResultDTO updateSR(SRDTO dto) {
    I18n.setLocaleForService(wsContext);
    return srOutsideBusiness.updateSR(dto);
  }

  @Override
  public ResultDTO insertSRWorklog(SRWorkLogDTO srWorklogDTO) {
    I18n.setLocaleForService(wsContext);
    return srOutsideBusiness.insertSRWorklog(srWorklogDTO);
  }

  @Override
  public List<GnocFileDto> getListGnocFileForSR(GnocFileDto gnocFileDto) {
    I18n.setLocaleForService(wsContext);
    return srOutsideBusiness.getListGnocFileForSR(gnocFileDto);
  }
  @Override
  public String updateStatusSRForProcess(String srId, String status) {
    I18n.setLocaleForService(wsContext);
    return srBusiness.updateStatusSRForProcess(srId,status);
  }

  @Override
  public List<SRConfigDTO> getByConfigGroup(String configGroup) {
    I18n.setLocaleForService(wsContext);
    return srOutsideBusiness.getByConfigGroup(configGroup);
  }


}
