package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.business.CrGeneralBusiness;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.ItemDataCRDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.UserCabCrForm;
import com.viettel.gnoc.ws.provider.WSGatePort;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CrGeneralService")
@Slf4j
public class CrGeneralController {

  @Autowired
  CrGeneralBusiness crGeneralBusiness;

  @Autowired
  WSGatePort wsGatePort;

  @GetMapping("/getListSubcategoryCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListSubcategoryCBB() {
    List<ItemDataCRInside> data = crGeneralBusiness.getListSubcategoryCBB();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListImpactSegmentCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListImpactSegmentCBB() {
    List<ItemDataCRInside> data = crGeneralBusiness.getListImpactSegmentCBB();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListImpactAffectCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListImpactAffectCBB() {
    List<ItemDataCRInside> data = crGeneralBusiness.getListImpactAffectCBB();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListAffectedServiceCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListAffectedServiceCBB(Long form) {
    List<ItemDataCRInside> data = crGeneralBusiness.getListAffectedServiceCBB(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListAffectedServiceCBProxy/form{form}")
  public ResponseEntity<List<ItemDataCRInside>> getListAffectedServiceCBProxy(
      @PathVariable(value = "form") Long form) {
    List<ItemDataCRInside> data = crGeneralBusiness.getListAffectedServiceCBB(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListDutyTypeCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListDutyTypeCBB(
      @RequestBody CrImpactFrameInsiteDTO form) {
    if (form != null && StringUtils.isNotNullOrEmpty(form.getProxyLocale())) {
      setLocale(form.getProxyLocale());
    }
    List<ItemDataCRInside> data = crGeneralBusiness.getListDutyTypeCBB(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }


  @PostMapping("/getListDeviceTypeByImpactSegmentCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListDeviceTypeByImpactSegmentCBB(
      @RequestBody CrInsiteDTO form) {
    if (form != null && StringUtils.isNotNullOrEmpty(form.getProxyLocale())) {
      setLocale(form.getProxyLocale());
    }
    List<ItemDataCRInside> data = crGeneralBusiness.getListDeviceTypeByImpactSegmentCBB(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/actionGetListUser")
  public ResponseEntity<List<UsersInsideDto>> actionGetListUser(String deptId, String userId,
      String userName, String fullName, String staffCode, String deptName, String deptCode,
      String isAppraise) {
    List<UsersInsideDto> data = crGeneralBusiness
        .actionGetListUser(deptId, userId, userName, fullName, staffCode, deptName, deptCode,
            isAppraise);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListReturnCodeByActionCode")
  public ResponseEntity<List<ItemDataCRInside>> getListReturnCodeByActionCode(Long actionCode) {
    List<ItemDataCRInside> data = crGeneralBusiness
        .getListReturnCodeByActionCode(actionCode, I18n.getLocale());
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListActionCodeByCode")
  public ResponseEntity<List<ItemDataCRInside>> getListActionCodeByCode(String code) {
    List<ItemDataCRInside> data = crGeneralBusiness.getListActionCodeByCode(code, I18n.getLocale());
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getCreatedBySys")
  public ResponseEntity<List<ItemDataCRDTO>> getCreatedBySys(String crId) {
    List<ItemDataCRDTO> data = crGeneralBusiness.getCreatedBySys(crId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getCbbChildArray")
  public ResponseEntity<List<CfgChildArrayDTO>> getCbbChildArray(
      @RequestBody CfgChildArrayDTO cfgChildArrayDTO) {
    if (cfgChildArrayDTO != null && StringUtils
        .isNotNullOrEmpty(cfgChildArrayDTO.getProxyLocale())) {
      setLocale(cfgChildArrayDTO.getProxyLocale());
    }
    List<CfgChildArrayDTO> data = crGeneralBusiness.getCbbChildArray(cfgChildArrayDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListUserCab")
  public ResponseEntity<List<UserCabCrForm>> getListUserCab(String impactSegmentId,
      String executeUnitId) {
    List<UserCabCrForm> data = crGeneralBusiness.getListUserCab(impactSegmentId, executeUnitId);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/getListUnit")
  public ResponseEntity<List<UnitDTO>> getListUnit(@RequestBody UnitDTO unitDTO) {
    List<UnitDTO> data = crGeneralBusiness.getListUnit(unitDTO);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/insertCrCreatedFromOtherSystem")
  public ResponseEntity<ResultInSideDto> insertCrCreatedFromOtherSystem(
      @RequestBody CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO) {
    ResultInSideDto result = crGeneralBusiness
        .insertCrCreatedFromOtherSystem(crCreatedFromOtherSysDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }


  @GetMapping("/getListSubcategoryCBBLocaleProxy/locale{locale}")
  public ResponseEntity<List<ItemDataCRInside>> getListSubcategoryCBBLocaleProxy(
      @PathVariable(value = "locale") String locale) {
    setLocale(locale);
    List<ItemDataCRInside> data = crGeneralBusiness.getListSubcategoryCBB();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListImpactSegmentCBBLocaleProxy/locale{locale}")
  public ResponseEntity<List<ItemDataCRInside>> getListImpactSegmentCBBLocaleProxy(
      @PathVariable(value = "locale") String locale) {
    setLocale(locale);
    List<ItemDataCRInside> data = crGeneralBusiness.getListImpactSegmentCBB();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListImpactAffectCBBLocaleProxy/locale{locale}")
  public ResponseEntity<List<ItemDataCRInside>> getListImpactAffectCBBLocaleProxy(
      @PathVariable(value = "locale") String locale) {
    setLocale(locale);
    List<ItemDataCRInside> data = crGeneralBusiness.getListImpactAffectCBB();
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListAffectedServiceCBBLocaleProxy/form{form}/locale{locale}")
  public ResponseEntity<List<ItemDataCRInside>> getListAffectedServiceCBBLocaleProxy(
      @PathVariable(value = "form") Long form, @PathVariable(value = "locale") String locale) {
    setLocale(locale);
    List<ItemDataCRInside> data = crGeneralBusiness.getListAffectedServiceCBB(form);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/getListSessionUCTTCBB")
  public ResponseEntity<List<ItemDataCR>> getListSessionUCTTCBB() throws Exception {
    List<ItemDataCR> data = wsGatePort
        .getListSessionUCTT(TicketProvider.getUserToken().getUserName());
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @GetMapping("/loadSearchTypeByRole")
  public ResponseEntity<ResultInSideDto> loadSearchTypeByRole() {
    ResultInSideDto result = crGeneralBusiness.loadSearchTypeByRole();
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  private void setLocale(String strLocale) {
    Locale locale = getLocale(strLocale);
    if (locale != null) {
      LocaleContextHolder.setLocale(locale);
    } else {
      LocaleContextHolder.setLocale(new Locale("vi", "VN"));
    }
  }

  private Locale getLocale(String locale) {
    Locale localeCus = null;
    try {
      if (locale != null && "en".equalsIgnoreCase(locale) || "en_us".equalsIgnoreCase(locale)) {
        localeCus = new Locale("en", "US");
      } else if (locale != null && "vi".equalsIgnoreCase(locale) || "vi_vn"
          .equalsIgnoreCase(locale)) {
        localeCus = new Locale("vi", "VN");
      } else if (locale != null && "lo".equalsIgnoreCase(locale) || "lo_la"
          .equalsIgnoreCase(locale)) {
        localeCus = new Locale("lo", "LA");
      } else {
        localeCus = new Locale("vi", "VN");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return localeCus;
  }

}
