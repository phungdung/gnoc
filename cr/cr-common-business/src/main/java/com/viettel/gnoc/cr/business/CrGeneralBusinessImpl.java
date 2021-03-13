package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CR_SEARCH_TYPE;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.ItemDataCRDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.ObjResponse;
import com.viettel.gnoc.cr.dto.UserCabCrForm;
import com.viettel.gnoc.cr.repository.CrGeneralRepository;
import com.viettel.gnoc.cr.util.SessionIdentifierGenerator;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import viettel.passport.client.UserToken;
import viettel.passport.client.UserTokenSSO2;
import viettel.passport.service.SSOServiceUtils;

@Service
@Slf4j
@Transactional
public class CrGeneralBusinessImpl implements CrGeneralBusiness {

  @Value("${application.conf.domainCode: null}")
  private String domainCode1;

  @Value("${application.conf.versionApp: null}")
  private String versionApp1;

  @Value("${application.conf.flagRequire: null}")
  private String flagRequire;

  @Value("${application.passport.service.url: null}")
  private String url;

  @Value("${application.conf.ticketServiceUrl: null}")
  private String ticketServiceUrl1;


  @Autowired
  CrGeneralRepository crGeneralRepository;

  @Autowired
  CrGeneralBusiness crGeneralBusiness;

  @Autowired
  UserRepository userRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public List<ItemDataCRInside> getListSubcategoryCBB() {
    return crGeneralRepository.getListSubcategoryCBB(I18n.getLocale());
  }

  @Override
  public List<ItemDataCRInside> getListImpactSegmentCBB() {
    return crGeneralRepository.getListImpactSegmentCBB(I18n.getLocale());
  }

  @Override
  public List<ItemDataCRInside> getListImpactAffectCBB() {
    return crGeneralRepository.getListImpactAffectCBB(I18n.getLocale());
  }

  @Override
  public List<ItemDataCRInside> getListAffectedServiceCBB(Long form) {
    return crGeneralRepository.getListAffectedServiceCBB(form, I18n.getLocale());
  }

  @Override
  public List<ItemDataCRInside> getListDutyTypeCBB(CrImpactFrameInsiteDTO form) {
    return crGeneralRepository.getListDutyTypeCBB(form, I18n.getLocale());
  }

  @Override
  public List<ItemDataCRInside> getListDeviceTypeByImpactSegmentCBB(CrInsiteDTO crDTO) {
    return crGeneralRepository.getListDeviceType(crDTO, I18n.getLocale());
  }

  @Override
  public List<ItemDataCRInside> getListActionCodeByCode(String code, String locale) {
    return crGeneralRepository.getListActionCodeByCode(code, locale);
  }

  @Override
  public List<UsersInsideDto> actionGetListUser(String deptId, String userId, String userName,
      String fullName, String staffCode, String deptName, String deptCode, String isAppraise) {
    return crGeneralRepository
        .actionGetListUser(deptId, userId, userName, fullName, staffCode, deptName, deptCode,
            isAppraise);
  }

  @Override
  public List<ItemDataCRInside> getListReturnCodeByActionCode(Long actionCode, String locale) {
    return crGeneralRepository.getListReturnCodeByActionCode(actionCode, locale);
  }

  @Override
  public List<ItemDataCRDTO> getCreatedBySys(String crId) {
    return crGeneralRepository.getCreatedBySys(crId);
  }

  @Override
  public List<CfgChildArrayDTO> getCbbChildArray(CfgChildArrayDTO dto) {
    return crGeneralRepository.getCbbChildArray(dto);
  }

  @Override
  public List<UserCabCrForm> getListUserCab(String impactSegmentId, String executeUnitId) {
    return crGeneralRepository.getListUserCab(impactSegmentId, executeUnitId);
  }

  @Override
  public List<UnitDTO> getListUnit(UnitDTO unitDTO) {
    return crGeneralRepository.getListUnit(unitDTO);
  }

  @Override
  public ResultInSideDto insertCrCreatedFromOtherSystem(
      CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO) {
    String msg = crGeneralRepository.insertCrCreatedFromOtherSystem(crCreatedFromOtherSysDTO);
    return new ResultInSideDto(null, msg, msg);
  }

  // anhlp add
  @Override
  public ObjResponse doLogin(
      String versionApp, String locale,
      String userName, String password) {
    ObjResponse result = new ObjResponse();
    if (StringUtils.isStringNullOrEmpty(locale)) {
      locale = "vi_VN";
    }
    Locale locale1 = new Locale(locale);
    if (locale != null && "en".equalsIgnoreCase(locale) || "en_us"
        .equalsIgnoreCase(locale)) {
      locale1 = new Locale("en", "US");
    } else if (locale != null && "vi".equalsIgnoreCase(locale) || "vi_vn"
        .equalsIgnoreCase(locale)) {
      locale1 = new Locale("vi", "VN");
    } else if (locale != null && "lo".equalsIgnoreCase(locale) || "lo_la"
        .equalsIgnoreCase(locale)) {
      locale1 = new Locale("lo", "LA");
    }
    try {
      String domainCode = domainCode1;
      try {
        String versionServerStr = versionApp1;
        if (versionApp != null && versionServerStr != null) {
          String flagRequireStr = flagRequire;
          Float versionClient = Float.valueOf(StringUtils.removeDotVersion(versionApp));
          Float versionServer = Float.valueOf(StringUtils.removeDotVersion(versionServerStr));
          log.info("VersionClient: " + versionClient + " | VersionServer: " + versionServer);
          if (versionClient.compareTo(versionServer) < 0) {
            result.setMessage(I18n.getLanguageByLocale(locale1, "alert.new.version"));
            if (flagRequireStr != null && "false".equalsIgnoreCase(flagRequireStr)) {
              result.setResponseStatus(Constants.CR_RETURN_MESSAGE.WARNING);
            } else {//Bat buoc update
              result.setResponseStatus(Constants.CR_RETURN_MESSAGE.ERROR);
              return result;
            }
          }
        } else {
          result.setMessage(I18n.getLanguageByLocale(locale1, "cannot.check.version"));
          result.setResponseStatus(Constants.CR_RETURN_MESSAGE.ERROR);
          return result;
        }
      } catch (Exception e) {
        log.debug(e.getMessage(), e);
      }
      SessionIdentifierGenerator sessionid = new SessionIdentifierGenerator();
      UserToken userToken = validate(userName, password, domainCode);
      if (userToken != null) {
        String uuid = sessionid.nextSessionId();
        result.setSessionId(uuid);
        ObjResponse obj = crGeneralBusiness.getUserInfoForMobile(userName, locale);
        result.setFullName(obj.getFullName());
        result.setTotalRow(obj.getTotalRow());
        result.setTimeZoneOffset(obj.getTimeZoneOffset());
        result.setTimeZoneName(obj.getTimeZoneName());
        result.setUserId(obj.getUserId());
        result.setUnitId(obj.getUnitId());
        result.setUnitName(obj.getUnitName());
        result.setUserName(obj.getUserName());
        if (result.getResponseStatus() != Constants.CR_RETURN_MESSAGE.WARNING) {
          crGeneralBusiness.insertSession(obj.getUserId(), uuid);
          result.setResponseStatus(Constants.CR_RETURN_MESSAGE.SUCCESS);

        }
      } else {
        result.setMessage(I18n.getLanguageByLocale(locale1, "crmobile.alert.wrongaccount"));
        result.setResponseStatus(Constants.CR_RETURN_MESSAGE.ERROR);
      }

    } catch (Exception ex) {
      log.debug(ex.getMessage(), ex);
      result.setResponseStatus(Constants.CR_RETURN_MESSAGE.ERROR);
      result.setMessage(I18n.getLanguageByLocale(locale1, "an.err.while.login"));

    }
    try {
      String desc = "[Login Ver " + versionApp + "] ";
      if (result != null) {
        if (result.getMessage() != null) {
          desc += result.getMessage();
        }
        if (result.getResponseStatus() != null) {
          desc += "|" + result.getResponseStatus();
        }
      }
      crGeneralBusiness.saveLogAction(userName, desc);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  // anhlp add
  private UserToken validate(String username, String password, String appCode) {
    try {
      String ticketServiceUrl = ticketServiceUrl1;
      UserToken userToken = new UserToken();
      String params =
          "username=" + URLEncoder.encode(username) + "&password=" + URLEncoder.encode(password)
              + "&token=true";
      String s = SSOServiceUtils.sendHtpps(params, ticketServiceUrl);
      String jsonString = SSOServiceUtils.getJsonData(s);
      UserTokenSSO2 userTokenSSO2 = UserTokenSSO2.initWithJSON(jsonString);
      userToken.setStatus(Long.valueOf(userTokenSSO2.getStatus()));
      userToken.setStaffCode(userTokenSSO2.getStaffCode());
      userToken.setDeptId(userTokenSSO2.getDeptId());
      userToken.setFullName(userTokenSSO2.getFullName());
      userToken.setUserName(userTokenSSO2.getUserName());
      userToken.setUserID(userTokenSSO2.getUserId());
      userToken.setEmail(userTokenSSO2.getEmail());
      userToken.setCellphone(userTokenSSO2.getCellPhone());

      return userToken;
    } catch (Exception var22) {
      log.error("lay thong tin tu VsaadminService.wsdl error: ", var22);
      return null;
    }
  }

  // anhlp add
  @Override
  public ObjResponse getUserInfoForMobile(String username, String locale) {
    ObjResponse obj = new ObjResponse();
    UsersDTO usersDTO = crGeneralRepository.getUserInfoForMobile(username);

    if (usersDTO != null) {
//                int count = dbDao.getCountNewTopic(session, usersBO.getUserId());
//                obj.setTotalRow(count);
      obj.setUserId(usersDTO.getUserId());
      obj.setUnitId(usersDTO.getUnitId());
      obj.setFullName(usersDTO.getFullname());
      obj.setUnitName(usersDTO.getUnitName());
      obj.setUserName(usersDTO.getUsername());
      obj.setTimeZoneOffset(usersDTO.getUserTimeZone());
//            obj.setTimeZoneName(usersDTO.getTimeZoneName());
      obj.setResponseStatus(Constants.CR_RETURN_MESSAGE.SUCCESS);
    } else {
      obj.setResponseStatus(Constants.CR_RETURN_MESSAGE.ERROR);
      obj.setMessage(
          I18n.getMessages("crmobile.alert.wrongaccount"));
    }
    return obj;
  }

  // anhlp add
  @Override
  public void insertSession(String userId, String sessionId) {
    crGeneralRepository.insertSession(userId, sessionId);
  }

  // anhlp add
  @Override
  public void saveLogAction(String userName, String description) {
    crGeneralRepository.saveLogAction(userName, description);
  }

  // anhlp add
  @Override
  public ObjResponse doLoginV2(
      String versionApp, String locale,
      String userName, String password) {
    ObjResponse result = new ObjResponse();
    if (StringUtils.isStringNullOrEmpty(locale)) {
      locale = "vi_VN";
    }
    Locale locale1 = new Locale(locale);
    if (locale != null && "en".equalsIgnoreCase(locale) || "en_us"
        .equalsIgnoreCase(locale)) {
      locale1 = new Locale("en", "US");
    } else if (locale != null && "vi".equalsIgnoreCase(locale) || "vi_vn"
        .equalsIgnoreCase(locale)) {
      locale1 = new Locale("vi", "VN");
    } else if (locale != null && "lo".equalsIgnoreCase(locale) || "lo_la"
        .equalsIgnoreCase(locale)) {
      locale1 = new Locale("lo", "LA");
    }
    try {
      String domainCode = domainCode1;
      try {
        String versionServerStr = versionApp1;
        if (versionApp != null && versionServerStr != null) {
          String flagRequireStr = flagRequire;
          Float versionClient = Float.valueOf(StringUtils.removeDotVersion(versionApp));
          Float versionServer = Float.valueOf(StringUtils.removeDotVersion(versionServerStr));
          log.info("VersionClient: " + versionClient + " | VersionServer: " + versionServer);
          if (versionClient.compareTo(versionServer) < 0) {
            result.setMessage(I18n.getLanguageByLocale(locale1, "alert.new.version"));
            if (flagRequireStr != null && "false".equalsIgnoreCase(flagRequireStr)) {
              result.setResponseStatus(Constants.CR_RETURN_MESSAGE.WARNING);
            } else {//Bat buoc update
              result.setResponseStatus(Constants.CR_RETURN_MESSAGE.ERROR);
              return result;
            }
          }
        } else {
          result.setMessage(I18n.getLanguageByLocale(locale1, "cannot.check.version"));
          result.setResponseStatus(Constants.CR_RETURN_MESSAGE.ERROR);
          return result;
        }
      } catch (Exception e) {
        log.debug(e.getMessage(), e);
      }
      SessionIdentifierGenerator sessionid = new SessionIdentifierGenerator();
      UserToken userToken = validate(userName, password, domainCode);

      if (userToken != null) {
        String uuid = sessionid.nextSessionId();
        result.setSessionId(uuid);
        ObjResponse obj = crGeneralBusiness.getUserInfoForMobile(userName, locale);
        result.setFullName(obj.getFullName());
        result.setTotalRow(obj.getTotalRow());
        result.setTimeZoneOffset(obj.getTimeZoneOffset());
        result.setTimeZoneName(obj.getTimeZoneName());
        result.setUserId(obj.getUserId());
        result.setUnitId(obj.getUnitId());
        result.setUnitName(obj.getUnitName());
        result.setUserName(obj.getUserName());
        if (result.getResponseStatus() != Constants.CR_RETURN_MESSAGE.WARNING) {

          crGeneralBusiness.insertSessionV2(obj.getUserId(), obj.getUnitId(), uuid);
          result.setResponseStatus(Constants.CR_RETURN_MESSAGE.SUCCESS);

        }

      } else {
        result.setMessage(I18n.getLanguageByLocale(locale1, "crmobile.alert.wrongaccount"));
        result.setResponseStatus(Constants.CR_RETURN_MESSAGE.ERROR);
      }
    } catch (Exception ex) {
      log.debug(ex.getMessage(), ex);
      result.setResponseStatus(Constants.CR_RETURN_MESSAGE.ERROR);
      result.setMessage(I18n.getLanguageByLocale(locale1, "an.err.while.login"));
    }
    try {
      String desc = "[Login Ver " + versionApp + "] ";
      if (result != null) {
        if (result.getMessage() != null) {
          desc += result.getMessage();
        }
        if (result.getResponseStatus() != null) {
          desc += "|" + result.getResponseStatus();
        }
      }
      crGeneralBusiness.saveLogAction(userName, desc);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  // anhlp add
  @Override
  public void insertSessionV2(String userId, String unitId, String sessionId) {
    crGeneralRepository.insertSessionV2(userId, unitId, sessionId);
  }

  @Override
  public List<ItemDataCR> getListScopeOfUserForAllRole(CrDTO crDTO, String locale) {
    List<ItemDataCR> lst = new ArrayList<>();
    try {
      if (crDTO != null) {
        if (StringUtils.isStringNullOrEmpty(crDTO.getUserLogin())) {
          return lst;
        }
        if (StringUtils.isStringNullOrEmpty(crDTO.getUserLoginUnit())) {
          return lst;
        }
        if (StringUtils.isStringNullOrEmpty(crDTO.getSearchType())) {
          return lst;
        }
        if (CR_SEARCH_TYPE.WAIT_CAB.toString().equals(crDTO.getSearchType())
            || Constants.CR_SEARCH_TYPE.QLTD.toString().equals(crDTO.getSearchType())) {
          Long deptId = Long.parseLong(crDTO.getUserLoginUnit().trim());
          return crGeneralRepository.getListScopeOfUserNewForServiceV2(deptId, locale);
        }

        if (CR_SEARCH_TYPE.Z78.toString().equals(crDTO.getSearchType())) {
          return crGeneralRepository.getListScopeOfUserOfCabOrZ78ForServiceV2(crDTO, locale);
        }

        if (Constants.CR_SEARCH_TYPE.CLOSE.toString().equals(crDTO.getSearchType())) {
          Long deptId = Long.parseLong(crDTO.getUserLoginUnit().trim());
          return crGeneralRepository.getListScopeOfUserNewForServiceV2(deptId, locale);
        }

        if (Constants.CR_SEARCH_TYPE.SCHEDULE.toString().equals(crDTO.getSearchType())) {
          Long deptId = Long.parseLong(crDTO.getUserLoginUnit().trim());
          return crGeneralRepository.getListScopeOfUserNewForServiceV2(deptId, locale);
        }

        if (Constants.CR_SEARCH_TYPE.VERIFY.toString().equals(crDTO.getSearchType())) {
          Long deptId = Long.parseLong(crDTO.getUserLoginUnit().trim());
          return crGeneralRepository.getListScopeOfUserNewForServiceV2(deptId, locale);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<ItemDataCR> getListImpactSegmentCBBForServiceV2(String locale) {
    return crGeneralRepository.getListImpactSegmentCBBForServiceV2(locale);
  }

  @Override
  public List<ItemDataCR> getListSubcategoryCBBForServiceV2(String locale) {
    return crGeneralRepository.getListSubcategoryCBBForServiceV2(locale);
  }

  @Override
  public List<UsersDTO> actionGetListUserForService(String deptId, String userId, String userName,
      String fullName, String staffCode, String deptName, String deptCode, String isAppraise) {
    return crGeneralRepository
        .actionGetListUserForService(deptId, userId, userName, fullName, staffCode, deptName,
            deptCode, isAppraise);
  }

  @Override
  public List<ItemDataCR> getListReturnCodeByActionCodeForService(String actionCode,
      String locale) {
    Long id = null;
    try {
      id = Long.valueOf(actionCode);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return crGeneralRepository.getListReturnCodeByActionCodeForService(id, locale);
  }

  @Override
  public List<ItemDataCR> getListAffectedServiceCBB(Object form, String locale) {
    return crGeneralRepository.getListAffectedServiceCBBForService(form, locale);
  }

  @Override
  public List<ItemDataCR> getListImpactAffectCBB(Object form, String locale) {
    return crGeneralRepository.getListImpactAffectCBBForService(form, locale);
  }

  @Override
  public List<ItemDataCR> getListDutyTypeCBB(CrImpactFrameDTO form, String locale) {
    return crGeneralRepository.getListDutyTypeCBB(form, locale);
  }

  @Override
  public List<ItemDataCR> getListDeviceTypeCBB(Object form, String locale) {
    return crGeneralRepository.getListDeviceTypeCBB(form, locale);
  }

  @Override
  public List<ItemDataCR> getListDeviceTypeByImpactSegmentCBB(CrDTO form, String locale) {
    return crGeneralRepository.getListDeviceType(form, locale);
  }

  @Override
  public List<ItemDataCR> getListActionCodeByCodeForService(String code, String locale) {
    return crGeneralRepository.getListActionCodeByCodeForService(code, locale);
  }

  @Override
  public ResultInSideDto loadSearchTypeByRole() {
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    String ret = Constants.CR_SEARCH_TYPE.LOOKUP.toString();

    Long unitIdLong = userToken.getDeptId();
    Long userIdLong = userToken.getUserID();
    if (unitIdLong != null && userIdLong != null) {
      ret = loadSearchType(userIdLong);
    }
    if (Constants.CR_SEARCH_TYPE.CREATE_EDIT.toString().equals(ret)) {
      List<ItemDataCRInside> lst = crGeneralRepository
          .getListScopeOfUserNew(unitIdLong, I18n.getLocale());
      if (lst != null && lst.size() > 0) {
        ret = Constants.CR_SEARCH_TYPE.VERIFY.toString();
      }
    }
    resultInSideDto.setMessage(ret);
    return resultInSideDto;
  }

  private String loadSearchType(Long userId) {
    String ret = Constants.CR_SEARCH_TYPE.CREATE_EDIT.toString();
    boolean isManage = userRepository.isManagerOfUnits(userId);
    if (isManage) {
      ret = Constants.CR_SEARCH_TYPE.APPROVE.toString();
    }
    return ret;
  }

  @Override
  public List<ItemDataCR> getCreatedBySysMobile(String crId) {
    return crGeneralRepository.getCreatedBySysMobile(crId);
  }
}
