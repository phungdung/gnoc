package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.commons.business.GnocFileBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.business.CrAffectedServiceDetailsBusiness;
import com.viettel.gnoc.cr.business.CrAlarmBusiness;
import com.viettel.gnoc.cr.business.CrApprovalDepartmentBusiness;
import com.viettel.gnoc.cr.business.CrBusiness;
import com.viettel.gnoc.cr.business.CrCableBusiness;
import com.viettel.gnoc.cr.business.CrFileAttachBusiness;
import com.viettel.gnoc.cr.business.CrGeneralBusiness;
import com.viettel.gnoc.cr.business.CrHisBusiness;
import com.viettel.gnoc.cr.business.CrImpactedNodesBusiness;
import com.viettel.gnoc.cr.business.CrMobileBusiness;
import com.viettel.gnoc.cr.business.CrProcessBusiness;
import com.viettel.gnoc.cr.dto.CrAffectedServiceDetailsDTO;
import com.viettel.gnoc.cr.dto.CrAlarmDTO;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentDTO;
import com.viettel.gnoc.cr.dto.CrCableDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachResultDTO;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrModuleDetailDTO;
import com.viettel.gnoc.cr.dto.CrProcessDTO;
import com.viettel.gnoc.cr.dto.CrVendorDetailDTO;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.ObjResponse;
import com.viettel.gnoc.cr.dto.UserCabCrForm;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import viettel.passport.client.UserToken;

@Service
@Slf4j
public class CrMobileServiceImpl implements CrMobileService {

  @Autowired
  CrMobileBusiness crMobileBusiness;
  @Autowired
  CrBusiness crBusiness;
  @Autowired
  CrGeneralBusiness crGeneralBusiness;
  @Autowired
  CrApprovalDepartmentBusiness crApprovalDepartmentBusiness;
  @Autowired
  CrFileAttachBusiness crFileAttachBusiness;
  @Autowired
  CrHisBusiness crHisBusiness;
  @Autowired
  CrImpactedNodesBusiness crImpactedNodesBusiness;
  @Autowired
  CrAlarmBusiness crAlarmBusiness;
  @Autowired
  CrCableBusiness crCableBusiness;
  @Autowired
  CrProcessBusiness crProcessBusiness;
  @Autowired
  CrAffectedServiceDetailsBusiness crAffectedServiceDetailsBusiness;
  @Autowired
  GnocFileBusiness gnocFileBusiness;
  @Resource
  private WebServiceContext wsContext;


  @Override
  public List<CrDTO> getListCRByCount(CrDTO crDTO) {
    I18n.setLocaleForService(wsContext);
    return crMobileBusiness.getListCRBySearchTypeCount(crDTO);
  }

  @Override
  public ObjResponse getListCRByMobileFix(CrDTO crDTO, int rowStart, int maxRow, String userName) {
    I18n.setLocaleForService(wsContext);

    ObjResponse obj = new ObjResponse();
    UsersDTO userInfo = crBusiness.getUserInfo(userName);
    if (userInfo != null) {
      crDTO.setUserLogin(userInfo.getUserId());
      crDTO.setUserLoginUnit(userInfo.getUnitId());
      obj = crMobileBusiness
          .getListCRBySearchTypePaggingMobile(crDTO, rowStart, maxRow, I18n.getLocale());
    }
    return obj;
  }

  @Override
  public List<ResultDTO> actionGetMonitoringParamFix(String userName, String searchChild,
      String startDate, String endDate) {
    I18n.setLocaleForService(wsContext);

    List<ResultDTO> lst = new ArrayList<>();
    UsersDTO userInfo = crBusiness.getUserInfo(userName);
    if (userInfo != null) {
      lst = crBusiness
          .actionGetProvinceMonitoringParamFix(userInfo.getUserId(), userInfo.getUnitId(),
              searchChild, startDate, endDate);
    }
    return lst;
  }

  @Override
  public List<ItemDataCR> getListScopeOfUserForAllRole(CrDTO crDTO) {
    I18n.setLocaleForService(wsContext);
    return crGeneralBusiness.getListScopeOfUserForAllRole(crDTO, I18n.getLocale());
  }

  @Override
  public List<ItemDataCR> getListImpactSegmentCBB(Object form) {
    I18n.setLocaleForService(wsContext);
    return crGeneralBusiness.getListImpactSegmentCBBForServiceV2(I18n.getLocale());
  }

  @Override
  public List<ItemDataCR> getListSubcategoryCBB(Object form) {
    I18n.setLocaleForService(wsContext);
    return crGeneralBusiness.getListSubcategoryCBBForServiceV2(I18n.getLocale());
  }

  @Override
  public List<CrDTO> getListCRForExport(CrDTO crDTO, String lstCrId,
      String earliestCrCreatedTimeStr,
      String earliestCrStartTimeStr, String latestCrStartTimeStr, String latestCrUpdateTimeStr,
      int rowStart, int maxRow) {
    if (crDTO != null) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      try {
        I18n.setLocaleForService(wsContext);
        Date earliestCrCreatedTime = null;
        if (StringUtils.isNotNullOrEmpty(earliestCrCreatedTimeStr)) {
          earliestCrCreatedTime = dateFormat.parse(earliestCrCreatedTimeStr);
        }
        Date earliestCrStartTime = null;
        if (StringUtils.isNotNullOrEmpty(earliestCrStartTimeStr)) {
          earliestCrStartTime = dateFormat.parse(earliestCrStartTimeStr);
        }
        Date latestCrStartTime = null;
        if (StringUtils.isNotNullOrEmpty(latestCrStartTimeStr)) {
          latestCrStartTime = dateFormat.parse(latestCrStartTimeStr);
        }

        Date latestCrUpdateTime = null;
        if (StringUtils.isNotNullOrEmpty(latestCrUpdateTimeStr)) {
          latestCrUpdateTime = dateFormat.parse(latestCrUpdateTimeStr);
        }
        CrInsiteDTO crInsiteDTO = crDTO.toModelInsiteDTO();
        if (crDTO != null && crInsiteDTO == null) {
          throw new RuntimeException();
        }
        return crBusiness.getListCRForExportServiceV2(crInsiteDTO, lstCrId, earliestCrCreatedTime,
            earliestCrStartTime, latestCrStartTime,
            latestCrUpdateTime, rowStart, maxRow, I18n.getLocale());
      } catch (Exception e) {
        log.error(e.getMessage(), e);

      }
    }
    return null;
  }


  @Override
  public List<CrApprovalDepartmentDTO> getListCrApprovalDepartmentDTO(
      CrApprovalDepartmentDTO crApprovalDepartmentDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    if (crApprovalDepartmentDTO != null) {
      return crApprovalDepartmentBusiness
          .onSearch(crApprovalDepartmentDTO, rowStart, maxRow, sortType, sortFieldList);
    }
    return null;
  }

  @Override
  public List<CrFilesAttachDTO> getListCrFilesAttachDTO(CrFilesAttachDTO crFilesAttachDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    if (crFilesAttachDTO != null) {
//      return crFileAttachBusiness
//          .search(crFilesAttachDTO, rowStart, maxRow, sortType, sortFieldList);
      return crFileAttachBusiness
          .getCrFileAttachForOutSide(crFilesAttachDTO, rowStart, maxRow, sortType, sortFieldList);
    }
    return null;
  }

  @Override
  public List<CrHisDTO> getListCrHisDTO(CrHisDTO crHisDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    if (crHisDTO != null) {
      return crHisBusiness.search(crHisDTO, rowStart, maxRow, sortType, sortFieldList);
    }
    return null;
  }

  @Override
  public List<CrImpactedNodesDTO> getLisNodeOfCR(Long crId, String crCreatedDate,
      String earlierStartTime, String nodeType, String saveType) {
    return crImpactedNodesBusiness
        .getLisNodeOfCRForOutSide(crId, crCreatedDate, earlierStartTime, nodeType, saveType);
  }

  @Override
  public List<CrImpactedNodesDTO> getListCrImpactedNodesDTO(CrImpactedNodesDTO crImpactedNodesDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    if (crImpactedNodesDTO != null) {
      return crImpactedNodesBusiness
          .onSearch(crImpactedNodesDTO, rowStart, maxRow, sortType, sortFieldList);
    }
    return null;
  }

  @Override
  public List<CrImpactedNodesDTO> getListCrImpactedNodes(CrImpactedNodesDTO crImpactedNodesDTO,
      Date startDate, Date earlierStartTime) {
    if (crImpactedNodesDTO != null) {
      return crImpactedNodesBusiness
          .getImpactedNodes(startDate, earlierStartTime, "0", crImpactedNodesDTO.getDeviceCode(),
              crImpactedNodesDTO.getDeviceName(), crImpactedNodesDTO.getIp());
    }
    return null;
  }

  @Override
  public List<CrDTO> getListPreApprovedCr(CrDTO crDTO) {
    return crMobileBusiness.getListPreApprovedCrOutSide(crDTO);
  }

  @Override
  public List<CrDTO> getListSecondaryCr(CrDTO crDTO) {
    return crBusiness.getListSecondaryCr(crDTO);
  }

  @Override
  public List<CrFilesAttachResultDTO> getListFileImportByProcess(
      CrFilesAttachDTO crFilesAttachDTO) {
    return crFileAttachBusiness.getListFileImportByProcess(crFilesAttachDTO);
  }

  @Override
  public List<CrModuleDetailDTO> getListModuleByCr(CrDTO cdto) {
    return crAlarmBusiness.getListModuleByCr(cdto == null ? null : cdto.toModelInsiteDTO());
  }

  @Override
  public List<CrVendorDetailDTO> getListVendorByCr(CrDTO cdto) {
    return crAlarmBusiness.getListVendorByCr(cdto == null ? null : cdto.toModelInsiteDTO());
  }

  @Override
  public List<CrAlarmDTO> getListAlarmByCr(CrDTO cdto) {
    return crAlarmBusiness.getListAlarmByCr(cdto);
  }

  @Override
  public List<CrCableDTO> getListCrCableDTO(CrCableDTO crCableDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    if (crCableDTO != null) {
      return crCableBusiness
          .getListCrCableDTO(crCableDTO, rowStart, maxRow, sortType, sortFieldList);
    }
    return null;
  }

  @Override
  public List<ItemDataCR> getListCrProcessCBB(CrProcessDTO form) {
    I18n.setLocaleForService(wsContext);
    return crProcessBusiness.getListCrProcessCBB(form, I18n.getLocale());
  }

  @Override
  public List<UserCabCrForm> getListUserCab(String impactSegmentId, String executeUnitId) {
    return crGeneralBusiness.getListUserCab(impactSegmentId, executeUnitId);
  }

  @Override
  public String actionApproveCR(CrDTO crDTO) {
    I18n.setLocaleForService(wsContext);
    CrInsiteDTO crInsiteDTO = null;
    try {
      crInsiteDTO = crDTO.toModelInsiteDTO();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (crInsiteDTO == null) {
      return RESULT.ERROR;
    }
    String rs = crBusiness.actionApproveCR(crInsiteDTO, I18n.getLocale());
    try {
      if ("SUCCESS".equals(rs)) {
        crMobileBusiness.actionApproveCRAfter(crDTO);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.ERROR;
    }
    return rs;
  }

  @Override
  public String actionVerify(CrDTO crDTO) {
    I18n.setLocaleForService(wsContext);
    CrInsiteDTO crInsiteDTO = null;
    try {
      crInsiteDTO = crDTO.toModelInsiteDTO();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (crInsiteDTO == null) {
      return RESULT.ERROR;
    }

    return crMobileBusiness.actionVerifyMobile(crDTO, I18n.getLocale());
  }

  @Override
  public String actionAppraiseCr(CrDTO crDTO) {
    I18n.setLocaleForService(wsContext);
    CrInsiteDTO crInsiteDTO = null;
    try {
      crInsiteDTO = crDTO.toModelInsiteDTO();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (crInsiteDTO == null) {
      return RESULT.ERROR;
    }
    String rs = crBusiness.actionAppraiseCr(crInsiteDTO, I18n.getLocale());
    try {
      if (RESULT.SUCCESS.equals(rs)) {
        rs = crMobileBusiness.actionAppraiseCRAfter(crDTO);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }

  @Override
  public String actionReceiveCr(CrDTO crDTO) {
    I18n.setLocaleForService(wsContext);
    CrInsiteDTO crInsiteDTO = null;
    try {
      crInsiteDTO = crDTO.toModelInsiteDTO();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (crInsiteDTO == null) {
      return RESULT.ERROR;
    }
    return crMobileBusiness.actionReceiveCr(crDTO);
  }

  @Override
  public String actionScheduleCr(CrDTO crDTO) {
    I18n.setLocaleForService(wsContext);
    CrInsiteDTO crInsiteDTO = null;
    try {
      crInsiteDTO = crDTO.toModelInsiteDTO();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (crInsiteDTO == null) {
      return RESULT.ERROR;
    }
    return crMobileBusiness.actionScheduleCr(crDTO);
  }

  @Override
  public String actionResolveCr(CrDTO crDTO) {
    I18n.setLocaleForService(wsContext);
    CrInsiteDTO crInsiteDTO = null;
    try {
      crInsiteDTO = crDTO.toModelInsiteDTO();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (crInsiteDTO == null) {
      return RESULT.ERROR;
    }
    return crMobileBusiness.actionResolveCr(crDTO);
  }

  @Override
  public String actionCloseCr(CrDTO crDTO) {
    I18n.setLocaleForService(wsContext);
    return crMobileBusiness.actionCloseCr(crDTO);
  }

  @Override
  public String actionCancelCr(CrDTO crDTO) {
    I18n.setLocaleForService(wsContext);
    CrInsiteDTO crInsiteDTO = null;
    try {
      crInsiteDTO = crDTO.toModelInsiteDTO();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (crInsiteDTO == null) {
      return RESULT.ERROR;
    }
    return crBusiness.actionCancelCr(crInsiteDTO);
  }

  @Override
  public String actionUpdateNotify(CrDTO crDTO, Long actionCode) {
    I18n.setLocaleForService(wsContext);
    CrInsiteDTO crInsiteDTO = null;
    try {
      crInsiteDTO = crDTO.toModelInsiteDTO();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (crInsiteDTO == null) {
      return RESULT.ERROR;
    }
    return crBusiness.actionUpdateNotify(crInsiteDTO, actionCode);
  }

  @Override
  public String actionCab(CrDTO crDTO) {
    I18n.setLocaleForService(wsContext);
    CrInsiteDTO crInsiteDTO = null;
    try {
      crInsiteDTO = crDTO.toModelInsiteDTO();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (crInsiteDTO == null) {
      return RESULT.ERROR;
    }
    return crMobileBusiness.actionCab(crInsiteDTO);
  }

  @Override
  public String actionEditCr(CrDTO crDTO) {
    return crMobileBusiness.actionEditCr(crDTO);
  }

  @Override
  public List<UsersDTO> actionGetListUser(String deptId, String userId, String userName,
      String fullName, String staffCode, String unitName, String unitCode, String isAppraise) {
    return crGeneralBusiness
        .actionGetListUserForService(deptId, userId, userName, fullName, staffCode, unitName,
            unitCode, isAppraise);
  }

  @Override
  public List<CrDTO> getCrByIdAndResolveStatus(List<Long> crIds, Long resolveStatus) {
    return crMobileBusiness.getCrByIdAndResolveStatuṣ̣(crIds, resolveStatus);
  }

  @Override
  public List<ItemDataCR> getListReturnCodeByActionCode(String actionCode) {
    I18n.setLocaleForService(wsContext);
    return crGeneralBusiness.getListReturnCodeByActionCodeForService(actionCode, I18n.getLocale());
  }

  @Override
  public List<ItemDataCR> getListImpactAffectCBB(Object form) {
    I18n.setLocaleForService(wsContext);
    return crGeneralBusiness.getListImpactAffectCBB(form, I18n.getLocale());
  }

  @Override
  public List<ItemDataCR> getListAffectedServiceCBB(Object form) {
    I18n.setLocaleForService(wsContext);
    return crGeneralBusiness.getListAffectedServiceCBB(form, I18n.getLocale());
  }

  @Override
  public List<ItemDataCR> getListDutyTypeCBB(CrImpactFrameDTO form) {
    I18n.setLocaleForService(wsContext);
    return crGeneralBusiness.getListDutyTypeCBB(form, I18n.getLocale());
  }

  @Override
  public List<ItemDataCR> getListDeviceTypeCBB(Object form) {
    I18n.setLocaleForService(wsContext);
    return crGeneralBusiness.getListDeviceTypeCBB(form, I18n.getLocale());
  }

  @Override
  public List<ItemDataCR> getListDeviceTypeByImpactSegmentCBB(CrDTO form) {
    I18n.setLocaleForService(wsContext);
    return crGeneralBusiness.getListDeviceTypeByImpactSegmentCBB(form, I18n.getLocale());
  }

  @Override
  public List<ItemDataCR> getListActionCodeByCode(String code) {
    I18n.setLocaleForService(wsContext);
    return crGeneralBusiness.getListActionCodeByCodeForService(code, I18n.getLocale());
  }

  @Override
  public CrFilesAttachDTO getFileByPath(String path) {
    return crMobileBusiness.getFileByPath(path);
  }

  @Override
  public CrDTO findCrById(Long id, UserTokenGNOCSimple userTokenGNOC) {
    I18n.setLocaleForService(wsContext);
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
      CrAffectedServiceDetailsDTO crAffectedServiceDetailsDTO = new CrAffectedServiceDetailsDTO();
      crAffectedServiceDetailsDTO.setCrId(id.toString());
      List<CrAffectedServiceDetailsDTO> lstCrAffectedServiceDetailsDTOs = crAffectedServiceDetailsBusiness
          .search(crAffectedServiceDetailsDTO, 0, 100, "", "");
      crDTO.setLstAffectedService(lstCrAffectedServiceDetailsDTOs);
      try {
        CrDTO crExtends = crMobileBusiness.getCrByIdExtends(id.toString());
        if (StringUtils.isNotNullOrEmpty(crDTO.getImpactAffect())) {
          List<ItemDataCR> lstImpactAffect = crGeneralBusiness
              .getListImpactAffectCBB(new Object(), I18n.getLocale());
          if (lstImpactAffect != null && lstImpactAffect.size() > 0) {
            ItemDataCR itemDataCR = lstImpactAffect.stream().
                filter(item -> crDTO.getImpactAffect().equals(String.valueOf(item.getValueStr())))
                .findFirst().orElse(null);
            if (itemDataCR != null) {
              crDTO.setImpactAffect(itemDataCR.getDisplayStr());
            }
          }
        }
        if (crExtends != null) {
          crDTO.setCrProcessName(crExtends.getCrProcessName());
          crDTO.setDeviceType(crExtends.getDeviceType());
          crDTO.setDutyType(crExtends.getDutyType());
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      return crDTO;
    }
    return null;
  }

  @Override
  public ObjResponse getListCRBySearchTypePagging(CrDTO crDTO, int start, int maxResult) {
    I18n.setLocaleForService(wsContext);
    return crBusiness.getListCRBySearchTypePagging(crDTO, start, maxResult, I18n.getLocale());
  }

  @Override
  public String actionAssignCab(CrDTO crDTO) {
    I18n.setLocaleForService(wsContext);
    return crMobileBusiness.actionAssignCabMobile(crDTO, I18n.getLocale());
  }

  @Override
  public List<ItemDataCR> getCreatedBySys(String crId) {
    return crGeneralBusiness.getCreatedBySysMobile(crId);
  }
}
