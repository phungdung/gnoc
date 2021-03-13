/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.cr.business.CrAffectedServiceDetailsBusiness;
import com.viettel.gnoc.cr.business.CrBusiness;
import com.viettel.gnoc.cr.dto.CrAffectedServiceDetailsDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import viettel.passport.client.UserToken;

/**
 * @author sondt20
 * @version 1.0
 * @since 8/20/2015 4:51 PM
 */
@Service
@Slf4j
public class CrServiceImpl implements CrService {

  @Resource
  private WebServiceContext wsContext;

  @Autowired
  CrBusiness crBusiness;

  @Autowired
  CrAffectedServiceDetailsBusiness crAffectedServiceDetailsBusiness;

  @Override
  public String actionScheduleCr(CrDTO crDTO) {
    setLocale();
    if (crDTO == null) {
      return RESULT.ERROR;
    }
    String rs = crBusiness.actionScheduleCrForServer(crDTO.toModelInsiteDTO(), I18n.getLocale());
//    getRequest().setAttribute("durationDB", LogDBHandler.getDbTime());
    return rs;
  }

  @Override
  public String actionResolveCr(CrDTO crDTO) {
    setLocale();
    if (crDTO == null) {
      return RESULT.ERROR;
    }
    String rs = crBusiness.actionResolveCrForService(crDTO.toModelInsiteDTO(), I18n.getLocale());
    //    getRequest().setAttribute("durationDB", LogDBHandler.getDbTime());
    return rs;
  }

  @Override
  public String actionCab(CrDTO crDTO) {
    setLocale();
    if (crDTO == null) {
      return RESULT.ERROR;
    }
    String rs = crBusiness.actionCabForServer(crDTO.toModelInsiteDTO(), I18n.getLocale());
    //    getRequest().setAttribute("durationDB", LogDBHandler.getDbTime());
    return rs;
  }

  @Override
  public String actionApproveCR(CrDTO crDTO) {
    setLocale();
    if (crDTO == null) {
      return RESULT.ERROR;
    }
    String rs = crBusiness.actionApproveServiceCR(crDTO.toModelInsiteDTO(), I18n.getLocale());
    //    getRequest().setAttribute("durationDB", LogDBHandler.getDbTime());
    return rs;
  }

  @Override
  public String actionUpdateNotify(CrDTO crDTO, Long actionCode) {
    try {
      setLocale();
      return crBusiness.actionUpdateNotify(crDTO.toModelInsiteDTO(), actionCode);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return RESULT.ERROR;
  }

  @Override
  public ResultDTO updateCrWithNoti(CrDTO crDTO) {
    setLocale();
    if (crDTO == null) {
      ResultDTO rs = new ResultDTO();
      rs.setKey(RESULT.ERROR);
      return rs;
    }
    return crBusiness.updateCrWithNoti(crDTO.toModelInsiteDTO(), I18n.getLocale());
  }

  @Override
  public String actionEditCr(CrDTO crDTO) {
    setLocale();
    if (crDTO == null) {
      return RESULT.ERROR;
    }
    return crBusiness.actionEditServiceCR(crDTO.toModelInsiteDTO(), I18n.getLocale());
  }

  @Override
  public String actionReceiveCr(CrDTO crDTO) {
    setLocale();
    if (crDTO == null) {
      return RESULT.ERROR;
    }
    String rs = crBusiness.actionReceiveServiceCR(crDTO.toModelInsiteDTO(), I18n.getLocale());
//    getRequest().setAttribute("durationDB", LogDBHandler.getDbTime());
    return rs;
  }

  @Override
  public String actionVerify(CrDTO crDTO) {
    setLocale();
    if (crDTO == null) {
      return RESULT.ERROR;
    }
    String rs = crBusiness.actionVerifyForServer(crDTO.toModelInsiteDTO(), I18n.getLocale());
//    getRequest().setAttribute("durationDB", LogDBHandler.getDbTime());
    return rs;
  }

  @Override
  public String actionAssignCab(CrDTO crDTO) {
    setLocale();
    if (crDTO == null) {
      return RESULT.ERROR;
    }
    return crBusiness.actionAssignCabServiceCR(crDTO.toModelInsiteDTO(), I18n.getLocale());
  }

  @Override
  public String actionAppraiseCr(CrDTO crDTO) {
    setLocale();
    if (crDTO == null) {
      return RESULT.ERROR;
    }
    String rs = crBusiness.actionAppraiseCrForServer(crDTO.toModelInsiteDTO(), I18n.getLocale());
//    getRequest().setAttribute("durationDB", LogDBHandler.getDbTime());
    return rs;
  }

  @Override
  public String actionCancelCr(CrDTO crDTO) {
    setLocale();
    if (crDTO == null) {
      return RESULT.ERROR;
    }
    return crBusiness.actionCancelCr(crDTO.toModelInsiteDTO());
  }

  @Override
  public String actionCloseCr(CrDTO crDTO) {
    setLocale();
    try {
      return crBusiness.actionCloseCr(crDTO.toModelInsiteDTO(), I18n.getLocale());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
//    getRequest().setAttribute("durationDB", LogDBHandler.getDbTime());
    return Constants.CR_RETURN_MESSAGE.ERROR;
  }

  @Override
  public CrDTO findCrById(Long id, UserTokenGNOCSimple userTokenGNOC) {
    if (id != null && id > 0) {
      UserToken userToken = new UserToken();
      if (userTokenGNOC != null) {
        userToken.setDeptId(userTokenGNOC.getUnitId());
        userToken.setUserID(userTokenGNOC.getUserId());
        userToken.setUserName(userTokenGNOC.getUserName());
        userToken.setTelephone(userTokenGNOC.getMobile());
        userToken.setFullName(userTokenGNOC.getFullName());
      }
      CrInsiteDTO crInsiteDTO = crBusiness.getCrById(id, userToken);
      CrDTO crDTO = crInsiteDTO.toCrDTO();
//            CrImpactedNodesDTO crImpactedNodesDTO = new CrImpactedNodesDTO();
//            crImpactedNodesDTO.setCrId(id.toString());
//            crImpactedNodesDTO.setInsertTime(crDTO.getCreatedDate());
//            List<CrImpactedNodesDTO> lstImpactNode = crImpactedNodesBusiness.search(crImpactedNodesDTO, 0, 100, "", "");
//            crDTO.setLstNetworkNodeId(lstImpactNode);
      CrAffectedServiceDetailsDTO crAffectedServiceDetailsDTO = new CrAffectedServiceDetailsDTO();
      crAffectedServiceDetailsDTO.setCrId(id.toString());
      List<CrAffectedServiceDetailsDTO> lstCrAffectedServiceDetailsDTOs = crAffectedServiceDetailsBusiness
          .search(crAffectedServiceDetailsDTO, 0, 100, "", "");
      crDTO.setLstAffectedService(lstCrAffectedServiceDetailsDTOs);
      return crDTO;
    }
    return null;
  }

  @Override
  public ResultDTO insertCr(CrDTO crDTO) {
    ResultInSideDto rs = crBusiness.createObject(crDTO.toModelInsiteDTO());
//    getRequest().setAttribute("durationDB", LogDBHandler.getDbTime());
    return rs.toResultDTO();

  }

  private void setLocale() {
    I18n.setLocaleForService(wsContext);
  }

}
