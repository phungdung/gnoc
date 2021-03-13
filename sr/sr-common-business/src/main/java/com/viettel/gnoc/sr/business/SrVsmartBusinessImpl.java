package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.SrCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.SR_STATUS;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SREvaluateDTO;
import com.viettel.gnoc.sr.dto.SRRoleActionDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.sr.dto.UnitSRCatalogDTO;
import com.viettel.gnoc.sr.repository.SRCatalogRepository2;
import com.viettel.gnoc.sr.repository.SRConfigRepository;
import com.viettel.gnoc.sr.repository.SREvaluateRepository;
import com.viettel.gnoc.sr.repository.SROutsideRepository;
import com.viettel.gnoc.sr.repository.SrRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class SrVsmartBusinessImpl implements SrVsmartBusiness {

  @Autowired
  SRConfigRepository srConfigRepository;

  @Autowired
  SrOutsideBusiness srOutsideBusiness;

  @Autowired
  SrBusiness srBusiness;

  @Autowired
  SrRepository srRepository;

  @Autowired
  SROutsideRepository srOutsideRepository;

  @Autowired
  SrCategoryServiceProxy srCategoryServiceProxy;

  @Autowired
  SRCatalogRepository2 srCatalogRepository2;

  @Autowired
  CatLocationRepository catLocationRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  SREvaluateRepository srEvaluateRepository;

  @Override
  public SRDTO getDetailSRForVSmart(String srId, String loginUser) {
    if (!StringUtils.isStringNullOrEmpty(loginUser)) {
      UsersDTO user = srOutsideBusiness
          .checkUserByUserCodeOrName(loginUser, Constants.SR_CONFIG.DICH_VU_VSMART);
      if (user == null) {
        return new SRDTO("0", I18n.getLanguage("sr.error.loginUserIncorrect"));
      } else {
        if (StringUtils.isStringNullOrEmpty(srId)) {
          return new SRDTO("0", I18n.getLanguage("sr.error.srIdNotNull"));
        } else {
          if (!StringUtils.isLong(srId)) {
            return new SRDTO("0", I18n.getLanguage("sr.error.cantFindSR"));
          }
          SRDTO dto = srOutsideRepository.getDetailSRForVSmart(srId, loginUser);
          if (dto == null) {
            return new SRDTO("0", I18n.getLanguage("sr.error.cantFindSR"));
          } else {
            if (dto.getCreatedUser().contains(loginUser) && Constants.SR_STATUS.CONCLUDED
                .equalsIgnoreCase(dto.getStatus())) {
              dto.setIsCloseAble("1");
            } else {
              dto.setIsCloseAble("0");
            }
            if (!Constants.SR_STATUS.CONCLUDED.equalsIgnoreCase(dto.getStatus())
                && !Constants.SR_STATUS.CLOSED.equalsIgnoreCase(dto.getStatus())
                && !Constants.SR_STATUS.REJECTED.equalsIgnoreCase(dto.getStatus())
                && !Constants.SR_STATUS.DRAFT.equalsIgnoreCase(dto.getStatus())) {
              if (!StringUtils.isStringNullOrEmpty(dto.getSrUser())) {
                if (dto.getSrUser().contains(loginUser)) {
                  dto.setIsUpdateAble("1");
                } else {
                  dto.setIsUpdateAble("0");
                }
              } else {
                SRDTO dtoTmp = srRepository.getDetailNoOffset(Long.valueOf(srId)).toOutsideDTO();
                SRRoleUserInSideDTO srRoleUserDTO = new SRRoleUserInSideDTO();
                srRoleUserDTO.setStatus("A");
                srRoleUserDTO.setUnitId(Long.valueOf(dtoTmp.getSrUnit()));
                srRoleUserDTO.setCountry(dtoTmp.getCountry());
                srRoleUserDTO.setRoleCode(dtoTmp.getRoleCode());
                List<SRRoleUserInSideDTO> lstSrUser = srCategoryServiceProxy
                    .getlistSRRoleUserDTO(srRoleUserDTO);
                boolean checkUpdate = false;
                for (SRRoleUserInSideDTO item : lstSrUser) {
                  if (loginUser.equalsIgnoreCase(item.getUsername())) {
                    checkUpdate = true;
                    break;
                  }
                }
                if (checkUpdate) {
                  dto.setIsUpdateAble("1");
                } else {
                  dto.setIsUpdateAble("0");
                }
              }
            } else {
              dto.setIsUpdateAble("0");
            }
            return dto;
          }
        }
      }
    } else {
      return new SRDTO("0", I18n.getLanguage("sr.error.loginUserNotNull"));
    }
  }

  @Override
  public ResultDTO updateSRForVSmart(SRDTO srInputDTO) {
    ResultDTO res = new ResultDTO();
    try {
      if (StringUtils.isNotNullOrEmpty(srInputDTO.getStationCode())) {
        srInputDTO.setInsertSource("VSMART-" + srInputDTO.getStationCode());
      }
      //<editor-fold desc="tao SR tu VSmart" defaultstate="collapsed">
      if (StringUtils.isStringNullOrEmpty(srInputDTO.getSrCode())) {
        res = srOutsideBusiness
            .createSRByConfigGroup(srInputDTO, Constants.SR_CONFIG.DICH_VU_VSMART);
        res.setId(null);
        res.setLstResult(null);
      }
      //</editor-fold>
      //<editor-fold desc="update SR tu VSmart : cap nhat nguoi xu ly hoac dong SR" defaultstate="collapsed">
      else {
        String id = srInputDTO.getSrCode()
            .substring(srInputDTO.getSrCode().lastIndexOf("_") + 1);//lay id
        if (!StringUtils.isLong(id)) {
          res.setKey("0");
          res.setMessage(I18n.getLanguage("sr.error.cantFindSR.VSMART")
              .replace("SR_CODE", srInputDTO.getSrCode()));
          return res;
        }
        SrInsiteDTO srTmp = srRepository.getDetailNoOffset(Long.valueOf(id));
        if (srTmp == null) {
          res.setKey("0");
          res.setMessage(I18n.getLanguage("sr.error.cantFindSR.VSMART")
              .replace("SR_CODE", srInputDTO.getSrCode()));
          return res;
        } else {
          SRDTO srDTO = srTmp.toOutsideDTO();
          srDTO.setInsertSource(srInputDTO.getInsertSource());
          List<SRCatalogDTO> lstVSmart = srOutsideBusiness
              .getListSRCatalogByConfigGroup(Constants.SR_CONFIG.DICH_VU_VSMART);
          List<SRCatalogDTO> lstVSmartNew = new ArrayList<>();
          for (SRCatalogDTO c : lstVSmart) {
            List<SRCatalogDTO> lstCatalog = srCatalogRepository2
                .getListSRCatalogDTO(new SRCatalogDTO(c.getServiceCode()));
            lstVSmartNew.addAll(lstCatalog);
          }
          boolean checkVSmart = false;
          Long flowExecute = null;
          if (lstVSmartNew != null && !lstVSmartNew.isEmpty()) {
            for (SRCatalogDTO dto : lstVSmartNew) {
              if (!StringUtils.isLongNullOrEmpty(dto.getServiceId()) && dto.getServiceId()
                  .toString()
                  .equals(srDTO.getServiceId())) {
                checkVSmart = true;
                if (!StringUtils.isLongNullOrEmpty(dto.getFlowExecute())) {
                  flowExecute = dto.getFlowExecute();
                }
                break;
              }
            }
            if (!checkVSmart) {
              res.setKey("0");
              res.setMessage(I18n.getLanguage("sr.error.srNotInVSmart")
                  .replaceAll("srCode", srInputDTO.getSrCode()));
              return res;
            }
            if ((StringUtils.isStringNullOrEmpty(srInputDTO.getSrUser()) && StringUtils
                .isStringNullOrEmpty(srInputDTO.getStatus()))
                || (!StringUtils.isStringNullOrEmpty(srInputDTO.getSrUser()) && !StringUtils
                .isStringNullOrEmpty(srInputDTO.getStatus()))) {
              res.setKey("0");
              res.setMessage(I18n.getLanguage("sr.error.chooseUpdateSR"));
              return res;
            } else {
              if (StringUtils.isStringNullOrEmpty(srInputDTO.getUpdatedUser())) {
                res.setKey("0");
                res.setMessage(I18n.getLanguage("sr.error.updatedUserIsNotNull"));
                return res;
              } else {
                UsersEntity userTKG = userRepository.getUserByUserName(srInputDTO.getUpdatedUser());
                if (userTKG == null || StringUtils.isStringNullOrEmpty(userTKG.getUsername())) {
                  res.setKey("0");
                  res.setMessage(I18n.getLanguage("sr.error.updatedUserIncorrect"));
                  return res;
                }
              }
              //<editor-fold desc="dong SR" defaultstate="collapsed">
              if (Constants.SR_STATUS.CLOSED.equalsIgnoreCase(srInputDTO.getStatus())) {
                if (Constants.SR_STATUS.CLOSED.equals(srDTO.getStatus())) {
                  res.setKey("0");
                  res.setMessage(I18n.getLanguage("sr.error.srAlreadyClosed"));
                  return res;
                }
                SREvaluateDTO srEvaluateDTO = new SREvaluateDTO();
                Long count = srTmp.getCountNok() != null ? srTmp.getCountNok() : 0L;
                if (StringUtils.isNotNullOrEmpty(srInputDTO.getReCreateSR()) && "1"
                    .equals(srInputDTO.getReCreateSR())) {
                  SRRoleActionDTO dtoActionUpdate = new SRRoleActionDTO();
                  dtoActionUpdate.setCountry(srDTO.getCountry());
                  dtoActionUpdate.setFlowId(flowExecute);
                  dtoActionUpdate.setCurrentStatus(SR_STATUS.ASSIGNED_PLANNING);
                  dtoActionUpdate.setRoleType("UPDATE_SR");
                  dtoActionUpdate.setGroupRole(Constants.SR_ROLE.ORIGINATOR);
                  List<SRRoleActionDTO> lstDataUpdateSR = srCategoryServiceProxy
                      .getListSRRoleActionDTO(dtoActionUpdate);
                  if (lstDataUpdateSR != null && !lstDataUpdateSR.isEmpty()) {
                    srDTO.setStatus(SR_STATUS.ASSIGNED_PLANNING);
                  } else {
                    srDTO.setStatus(SR_STATUS.EVALUATED);
                  }
                  if (StringUtils.isStringNullOrEmpty(srInputDTO.getEvaluate())) {
                    srEvaluateDTO.setEvaluate("NOK");
                  } else {
                    srEvaluateDTO.setEvaluate(srInputDTO.getEvaluate());
                  }
                  if (StringUtils.isNotNullOrEmpty(srInputDTO.getEvaluateReason())) {
                    srEvaluateDTO.setEvaluateReason(srInputDTO.getEvaluateReason());
                  }
                  count++;
                } else {
                  if (StringUtils.isStringNullOrEmpty(srInputDTO.getEvaluate())) {
                    srEvaluateDTO.setEvaluate("OK");
                  } else {
                    srEvaluateDTO.setEvaluate(srInputDTO.getEvaluate());
                  }
                  if (StringUtils.isNotNullOrEmpty(srInputDTO.getEvaluateReason())) {
                    srEvaluateDTO.setEvaluateReason(srInputDTO.getEvaluateReason());
                  }
                  srDTO.setStatus(Constants.SR_STATUS.CLOSED);
                }
                if (StringUtils.isStringNullOrEmpty(srInputDTO.getReviewId())) {
                  res.setKey("0");
                  res.setMessage(I18n.getLanguage("sr.error.reviewIdIsNotNull"));
                  return res;
                } else if (!"1".equals(srInputDTO.getReviewId())
                    && !"2".equals(srInputDTO.getReviewId())
                    && !"3".equals(srInputDTO.getReviewId())
                    && !"4".equals(srInputDTO.getReviewId())
                    && !"5".equals(srInputDTO.getReviewId())) {
                  res.setKey("0");
                  res.setMessage(I18n.getLanguage("sr.error.reviewIdIncorrect"));
                  return res;
                }
                if (!srInputDTO.getUpdatedUser().equals(srDTO.getCreatedUser())) {
                  res.setKey("0");
                  res.setMessage(I18n.getLanguage("sr.error.updatedUserIsNotCreatedUser"));
                  return res;
                }
                if (srEvaluateDTO != null && StringUtils
                    .isNotNullOrEmpty(srEvaluateDTO.getEvaluate())) {
                  srEvaluateDTO.setCreatedTime(new Date());
                  srEvaluateDTO.setCreatedUser(srDTO.getCreatedUser());
                  srEvaluateDTO.setSrId(srDTO.getSrId());
                  srEvaluateRepository.insertSREvaluate(srEvaluateDTO);
                }
                srDTO.setUpdatedUser(srDTO.getCreatedUser());
                srDTO.setUpdatedTime(DateTimeUtils.getSysDateTime());
                srDTO.setReviewId(srInputDTO.getReviewId());
                SrInsiteDTO srUpdate = srDTO.toInsideDTO();
                srUpdate.setCountNok(count);
                ResultInSideDto resUpdate = srBusiness.updateSR(srUpdate);
                if (!Constants.RESULT.SUCCESS.equals(resUpdate.getKey())) {
                  res.setKey("0");
                  res.setMessage(I18n.getLanguage("sr.error.closedSRFailed"));
                } else {
                  res = resUpdate.toResultDTO();
                  res.setMessage(res.getKey());
                  res.setId(null);
                  res.setKey("1");
                }
              } else if (!StringUtils.isStringNullOrEmpty(srInputDTO.getStatus())
                  && !Constants.SR_STATUS.CLOSED.equalsIgnoreCase(srInputDTO.getStatus())) {
                res.setKey("0");
                res.setMessage(I18n.getLanguage("sr.error.statusIncorrect"));
                return res;
              }
              //</editor-fold>
              //<editor-fold desc="cap nhat nguoi xu ly" defaultstate="collapsed">
              if (!StringUtils.isStringNullOrEmpty(srInputDTO.getSrUser())) {
                //cap nhat nguoi xu ly
                UsersEntity userTKG = userRepository.getUserByUserName(srInputDTO.getSrUser());
                if (userTKG == null || StringUtils.isStringNullOrEmpty(userTKG.getUsername())) {
                  res.setKey("0");
                  res.setMessage(I18n.getLanguage("sr.error.srUserIncorrect"));
                  return res;
                }
                if (!Constants.SR_STATUS.CONCLUDED.equals(srDTO.getStatus())
                    && !Constants.SR_STATUS.CLOSED.equals(srDTO.getStatus())) {
                  if (!StringUtils.isStringNullOrEmpty(srDTO.getSrUser()) && !srInputDTO
                      .getUpdatedUser().equals(srDTO.getSrUser())) {
                    res.setKey("0");
                    res.setMessage(I18n.getLanguage("sr.error.updatedUserIsNotSrUser"));
                    return res;
                  }
                  if (StringUtils.isStringNullOrEmpty(srDTO.getSrUser())) {
                    SRRoleUserInSideDTO updateUser = new SRRoleUserInSideDTO();
                    updateUser.setUsername(srInputDTO.getUpdatedUser());
                    updateUser.setUnitId(Long.valueOf(srDTO.getSrUnit()));
                    updateUser.setRoleCode(srDTO.getRoleCode());
                    updateUser.setStatus("A");
                    List<SRRoleUserInSideDTO> lstUser = srCategoryServiceProxy
                        .getlistSRRoleUserDTO(updateUser);
                    if (lstUser == null || lstUser.isEmpty()) {
                      res.setKey("0");
                      res.setMessage(I18n.getLanguage("sr.error.cantFindCreateUserInUnit")
                          .replaceAll("updateUser", srInputDTO.getUpdatedUser()));
                      return res;
                    }
                  }
                  SRRoleUserInSideDTO srRoleUserDTO = new SRRoleUserInSideDTO();
                  srRoleUserDTO.setUsername(srInputDTO.getSrUser());
                  srRoleUserDTO.setUnitId(Long.valueOf(srDTO.getSrUnit()));
                  srRoleUserDTO.setStatus("A");
                  srRoleUserDTO.setRoleCode(srDTO.getRoleCode());
                  List<SRRoleUserInSideDTO> lstUser = srCategoryServiceProxy
                      .getlistSRRoleUserDTO(srRoleUserDTO);
                  if (lstUser == null || lstUser.isEmpty()) {
                    res.setKey("0");
                    res.setMessage(I18n.getLanguage("sr.error.cantFindUserInUnit")
                        .replaceAll("userName", srInputDTO.getSrUser())
                        .replaceAll("srCode", srInputDTO.getSrCode()));
                    return res;
                  }
                  srDTO.setSrUser(srInputDTO.getSrUser());
                  srDTO.setUpdatedUser(srInputDTO.getUpdatedUser());
                  srDTO.setUpdatedTime(DateTimeUtils.getSysDateTime());
                  ResultInSideDto resultUpdate = srBusiness.updateSR(srDTO.toInsideDTO());
                  if (!Constants.RESULT.SUCCESS.equals(resultUpdate.getKey())) {
                    res.setKey("0");
                    res.setMessage(I18n.getLanguage("sr.error.updateSrUserFailed"));
                    return res;
                  } else {
                    res = resultUpdate.toResultDTO();
                    res.setMessage(res.getKey());
                    res.setId(null);
                    res.setKey("1");
                  }
                } else {
                  res.setKey("0");
                  res.setMessage(I18n.getLanguage("sr.error.cantUpdateSrUser")
                      .replaceAll("srCode", srInputDTO.getSrCode()));
                }
              }
              //</editor-fold>
            }
          } else {
            res.setKey("0");
            res.setMessage(I18n.getLanguage("sr.error.srNotInVSmart")
                .replaceAll("srCode", srInputDTO.getSrCode()));
          }
        }
      }
      //</editor-fold>
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res.setKey("0");
      res.setMessage(e.getMessage());
    }
    return res;
  }

  @Override
  public List<SRConfigDTO> getListSRStatusForVSmart(String userName) {
    List<SRConfigDTO> lstStatusNew = new ArrayList<>();
    List<SRConfigDTO> lstStatus = srConfigRepository.getByConfigGroup("STATUS");
    if (lstStatus != null && !lstStatus.isEmpty()) {
      for (SRConfigDTO item : lstStatus) {
        if ("A".equals(item.getStatus())) {
          SRConfigDTO configNew = new SRConfigDTO();
          configNew.setConfigCode(item.getConfigCode());
          configNew.setConfigName(item.getConfigName());
          lstStatusNew.add(configNew);
        }
      }
    } else {
      lstStatusNew.add(new SRConfigDTO("0",
          I18n.getLanguage("sr.error.listDataNull").replaceAll("list", "SR Status")));
    }
    return lstStatusNew;
  }

  @Override
  public List<SRDTO> getListSRForVSmart(SRDTO dto) {
    List<SRDTO> lstSR = new ArrayList<>();
    if (!StringUtils.isStringNullOrEmpty(dto.getLoginUser())) {
      UsersDTO user = srOutsideBusiness.checkUserByUserCodeOrName(dto.getLoginUser(), "");
      if (user == null) {
        lstSR.add(new SRDTO("0", I18n.getLanguage("sr.error.loginUserIncorrect")));
        return lstSR;
      }
      lstSR = srOutsideRepository.getListSRForVSmart(dto);
      if (lstSR == null || lstSR.isEmpty()) {
        lstSR = new ArrayList<>();
        lstSR.add(
            new SRDTO("0", I18n.getLanguage("sr.error.listDataNull").replaceAll("list", "SR")));
      }
    } else {
      lstSR.add(new SRDTO("0", I18n.getLanguage("sr.error.loginUserNotNull")));
    }
    return lstSR;
  }

  @Override
  public List<SRRoleUserDTO> getListSRUserForVSmart(String serviceCode, Long unitId,
      String roleCode, String country) {
    List<SRRoleUserDTO> lstSR = new ArrayList<>();
    if (!StringUtils.isStringNullOrEmpty(serviceCode)) {
      lstSR = srOutsideRepository.getListSRUserForVSmart(serviceCode, unitId, roleCode, country);
      if (lstSR == null || lstSR.isEmpty()) {
        lstSR = new ArrayList<>();
        lstSR.add(new SRRoleUserDTO(
            I18n.getLanguage("sr.error.listDataNull").replaceAll("list", "SR User"), "0"));
      }
    } else {
      lstSR.add(new SRRoleUserDTO(I18n.getLanguage("sr.error.serviceCodeIsNotNull"), "0"));
    }
    return lstSR;
  }

  @Override
  public List<SRCatalogDTO> getListSRCatalogForVSmart(String userName, String serviceGroup) {
    List<SRCatalogDTO> lstSR = srOutsideRepository
        .getListSRCatalogByConfigGroupVSMAT(Constants.SR_CONFIG.DICH_VU_VSMART, serviceGroup);
    if (lstSR == null || lstSR.isEmpty()) {
      lstSR = new ArrayList<>();
      lstSR.add(new SRCatalogDTO("0",
          I18n.getLanguage("sr.error.listDataNull").replaceAll("list", "SR Catalog")));
    }
    return lstSR;
  }

  @Override
  public List<SRConfigDTO> getSRReviewForVSmart(String userName) {
    List<SRConfigDTO> lstOld = srConfigRepository
        .getByConfigGroup(Constants.SR_CONFIG.REVIEW_CLOSE_SR);
    List<SRConfigDTO> lstNew = new ArrayList<>();
    if (lstOld != null && !lstOld.isEmpty()) {
      for (SRConfigDTO item : lstOld) {
        SRConfigDTO dtoNew = new SRConfigDTO();
        dtoNew.setConfigCode(item.getConfigCode());
        dtoNew.setConfigName(item.getConfigName());
        lstNew.add(dtoNew);
      }
    } else {
      lstNew.add(new SRConfigDTO("0",
          I18n.getLanguage("sr.error.listDataNull").replaceAll("list", "SR Review")));
    }
    return lstNew;
  }

  @Override
  public List<SRRoleUserDTO> getListRoleUserForVsmart(SRRoleUserDTO srRoleUserDTO) {
    List<SRRoleUserDTO> lst = new ArrayList<>();
    if (StringUtils.isStringNullOrEmpty(srRoleUserDTO.getCountry())) {
      lst.add(new SRRoleUserDTO("0", I18n.getLanguage("sr.error.countryIsNotNull")));
      return lst;
    }
    if (!StringUtils.isLong(srRoleUserDTO.getCountry())) {
      lst.add(new SRRoleUserDTO("0", I18n.getLanguage("sr.error.countryExist")));
      return lst;
    }
    try {
      Long countryId = Long.valueOf(srRoleUserDTO.getCountry());
      SrInsiteDTO locationSelect = srRepository.findNationByLocationId(countryId);
      if (locationSelect == null || StringUtils.isStringNullOrEmpty(locationSelect.getCountry())) {
        lst.add(new SRRoleUserDTO("0", I18n.getLanguage("sr.error.cantFindCountry")));
        return lst;
      }

      List<CatLocationDTO> lstLocation = catLocationRepository.getCatLocationByLevel("1");
      if (lstLocation == null || lstLocation.isEmpty()) {
        lst.add(new SRRoleUserDTO("0", I18n.getLanguage("sr.error.cantFindCountry")));
        return lst;
      }
      boolean error = true;
      for (CatLocationDTO item : lstLocation) {
        if (locationSelect.getCountry().contains(item.getLocationId())) {
          srRoleUserDTO.setCountry(item.getLocationId());
          error = false;
          break;
        }
      }
      if (error) {
        lst.add(new SRRoleUserDTO("0", I18n.getLanguage("sr.error.cantFindCountry")));
        return lst;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (StringUtils.isStringNullOrEmpty(srRoleUserDTO.getUnitId())) {
      lst.add(new SRRoleUserDTO("0", I18n.getLanguage("sr.error.unitIdIsNotNull")));
      return lst;
    } else if (!StringUtils.isLong(srRoleUserDTO.getUnitId())) {
      lst.add(new SRRoleUserDTO("0", I18n.getLanguage("sr.error.unitIdExist")));
      return lst;
    }
    try {
      UnitDTO unitExecute = unitRepository.findUnitById(Long.parseLong(srRoleUserDTO.getUnitId()));
      if (unitExecute == null || unitExecute.getUnitId() == null) {
        lst.add(new SRRoleUserDTO("0", I18n.getLanguage("sr.error.cantFindUnitId")));
        return lst;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (StringUtils.isNotNullOrEmpty(srRoleUserDTO.getServiceCode())) {
      SRCatalogDTO srCatalogDTO = new SRCatalogDTO();
      srCatalogDTO.setServiceCode(srRoleUserDTO.getServiceCode());
      srCatalogDTO.setExecutionUnit(srRoleUserDTO.getUnitId());
      srCatalogDTO.setCountry(srRoleUserDTO.getCountry());
      List<SRCatalogDTO> lstSrCatalog = srCatalogRepository2.getListCatalog(srCatalogDTO);
      if (lstSrCatalog != null && lstSrCatalog.size() > 0) {
        SRRoleUserInSideDTO dtoRole = new SRRoleUserInSideDTO();
        dtoRole.setRoleCode(lstSrCatalog.get(0).getRoleCode());
        dtoRole.setCountry(srRoleUserDTO.getCountry());
        dtoRole.setStatus("A");
        dtoRole.setUnitId(Long.parseLong(srRoleUserDTO.getUnitId()));
        List<SRRoleUserInSideDTO> lstRole = srCategoryServiceProxy.getlistSRRoleUserDTO(dtoRole);
        Map<String, String> map = new HashMap<>();
        for (SRRoleUserInSideDTO dtoResult : lstRole) {
          if (map.get(dtoResult.getRoleCode()) == null) {
            SRRoleUserDTO reusltDTO = new SRRoleUserDTO();
            reusltDTO.setRoleCode(dtoResult.getRoleCode());
            reusltDTO.setRoleName(dtoResult.getRoleName());
            lst.add(reusltDTO);
            map.put(dtoResult.getRoleCode(), dtoResult.getRoleName());
          }
        }
      }
    } else {
      lst = srOutsideRepository.getListRoleUserForVsmart(srRoleUserDTO);
    }
    if (lst == null || lst.isEmpty()) {
      lst = new ArrayList<>();
      lst.add(new SRRoleUserDTO(
          I18n.getLanguage("sr.error.listDataNull").replaceAll("list", "SR RoleUser"), "0"));
    }
    return lst;
  }

  @Override
  public List<UnitSRCatalogDTO> getListUnitSRCatalogForVsmart(SRCatalogDTO dto) {
    List<UnitSRCatalogDTO> lst = new ArrayList<>();
    if (StringUtils.isStringNullOrEmpty(dto.getServiceCode())) {
      lst.add(new UnitSRCatalogDTO(I18n.getLanguage("sr.error.serviceCodeIsNotNull")));
      return lst;
    } else {
      List<UnitSRCatalogDTO> lstCatalog = srCatalogRepository2.getListUnitSRCatalog(dto);
      if (lstCatalog == null || lstCatalog.isEmpty()) {
        lst.add(new UnitSRCatalogDTO(I18n.getLanguage("sr.error.serviceCodeIncorrect")));
        return lst;
      } else {
        return lstCatalog;
      }
    }
  }

  @Override
  public List<SRConfigDTO> getListServiceGrouprForVsmart() {
    return srOutsideRepository.getListServiceGrouprForVsmart();
  }
}
