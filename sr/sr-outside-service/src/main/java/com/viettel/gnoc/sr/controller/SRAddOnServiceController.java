package com.viettel.gnoc.sr.controller;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.sr.business.SrAddOnBusiness;
import com.viettel.gnoc.sr.business.SrOutsideBusiness;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "SRAddOnService")
@Slf4j
public class SRAddOnServiceController {

  @Autowired
  protected SrAddOnBusiness srAddOnBusiness;

  @Autowired
  protected SrOutsideBusiness srOutsideBusiness;

  @RequestMapping(value = "/getListSRCatalogByConfigGroup", method = RequestMethod.POST)
  public ResponseEntity<List<SRCatalogDTO>> getListSRCatalogByConfigGroup(
      @RequestParam("configGroup") String configGroup,
      @RequestHeader HttpHeaders headers) {
    if (headers.get("locale") != null && !headers.get("locale").isEmpty()) {
      I18n.setLocale(headers.get("locale").get(0));
    } else {
      I18n.setLocale("vi_VN");
    }
    return new ResponseEntity<>(
        srOutsideBusiness.getListSRCatalogByConfigGroupIBPMS(configGroup),
        HttpStatus.OK);
  }

  @RequestMapping(value = "/createSRByConfigGroup", method = RequestMethod.POST)
  public ResponseEntity<ResultDTO> createSRByConfigGroup(
      @RequestParam("configGroup") String configGroup,
      @RequestBody SRDTO srInputDTO,
      @RequestHeader HttpHeaders headers) {
    if (headers.get("locale") != null && !headers.get("locale").isEmpty()) {
      I18n.setLocale(headers.get("locale").get(0));
    } else {
      I18n.setLocale("vi_VN");
    }
    return new ResponseEntity<>(
        srAddOnBusiness.createSRByConfigGroup(srInputDTO, configGroup),
        HttpStatus.OK);
  }

  @RequestMapping(value = "/getDetailSRById", method = RequestMethod.POST)
  public ResponseEntity<SrInsiteDTO> getDetailSRById(@RequestBody SRDTO srdto,
      @RequestHeader HttpHeaders headers) {
    if (headers.get("locale") != null && !headers.get("locale").isEmpty()) {
      I18n.setLocale(headers.get("locale").get(0));
    } else {
      I18n.setLocale("vi_VN");
    }
    return new ResponseEntity<>(
        srOutsideBusiness.getDetailSRById(srdto.getSrId()),
        HttpStatus.OK);
  }

  @RequestMapping(value = "/updateSR", method = RequestMethod.POST)
  public ResponseEntity<ResultDTO> updateSR(@RequestBody SRDTO srdto,
      @RequestHeader HttpHeaders headers) {
    if (headers.get("locale") != null && !headers.get("locale").isEmpty()) {
      I18n.setLocale(headers.get("locale").get(0));
    } else {
      I18n.setLocale("vi_VN");
    }
    return new ResponseEntity<>(
        srOutsideBusiness.updateSRForIBPMSOutSide(srdto),
        HttpStatus.OK);
  }
}
