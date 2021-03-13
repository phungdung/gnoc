package com.viettel.gnoc.od.controller;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.od.business.OdBusiness;
import com.viettel.gnoc.od.dto.OdDTO;
import com.viettel.gnoc.od.dto.OdDTOSearch;
import com.viettel.gnoc.od.dto.OdTypeDTO;
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
@RequestMapping(Constants.OD_API_PATH_PREFIX + "OdService")
@Slf4j
public class OdServiceController {

  @Autowired
  protected OdBusiness odBusiness;

  @RequestMapping(value = "/insertOdFromOtherSystem", method = RequestMethod.POST)
  public ResponseEntity<ResultDTO> insertOdFromOtherSystem(@RequestBody OdDTOSearch odDTO,
      @RequestHeader HttpHeaders headers) {
    if (headers.get("locale") != null && !headers.get("locale").isEmpty()) {
      I18n.setLocale(headers.get("locale").get(0));
    } else {
      I18n.setLocale("vi_VN");
    }
    return new ResponseEntity<>(odBusiness.insertOdFromOtherSystem(odDTO), HttpStatus.OK);
  }

  @RequestMapping(value = "/getInforByODType", method = RequestMethod.POST)
  public ResponseEntity<OdTypeDTO> getInforByODType(@RequestParam("odTypeCode") String odTypeCode,
      @RequestHeader HttpHeaders headers) {
    if (headers.get("locale") != null && !headers.get("locale").isEmpty()) {
      I18n.setLocale(headers.get("locale").get(0));
    } else {
      I18n.setLocale("vi_VN");
    }
    return new ResponseEntity<>(odBusiness.getInforByODType(odTypeCode), HttpStatus.OK);
  }

  @RequestMapping(value = "/getDetailOdDTOById", method = RequestMethod.POST)
  public ResponseEntity<OdDTO> getDetailOdDTOById(@RequestBody OdDTO odDTO,
      @RequestHeader HttpHeaders headers) {
    if (headers.get("locale") != null && !headers.get("locale").isEmpty()) {
      I18n.setLocale(headers.get("locale").get(0));
    } else {
      I18n.setLocale("vi_VN");
    }
    return new ResponseEntity<>(odBusiness.getDetailOdDTOByIdForWS(odDTO.getOdId()), HttpStatus.OK);
  }
}
