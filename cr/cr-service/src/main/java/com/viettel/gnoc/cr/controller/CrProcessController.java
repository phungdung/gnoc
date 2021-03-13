package com.viettel.gnoc.cr.controller;

import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.business.CrProcessBusiness;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.CR_API_PATH_PREFIX + "CrProcessService")
@Slf4j
public class CrProcessController {

  @Autowired
  CrProcessBusiness crProcessBusiness;


  @PostMapping("/findCrProcessById")
  public ResponseEntity<CrProcessInsideDTO> findCrProcessById(Long id) {
    CrProcessInsideDTO dto = crProcessBusiness
        .findCrProcessById(id);
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  @PostMapping("/getListCrProcessCBB")
  public ResponseEntity<List<ItemDataCRInside>> getListCrProcessCBB(
      @RequestBody CrProcessInsideDTO dto) {
    if (dto != null && StringUtils.isNotNullOrEmpty(dto.getProxyLocale())) {
      setLocale(dto.getProxyLocale());
    }
    List<ItemDataCRInside> list = crProcessBusiness.getListCrProcessCBB(dto);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PostMapping("/getListCrProcessCBBLevel3")
  public ResponseEntity<List<CrProcessInsideDTO>> getListCrProcessCBBLevel3(
      @RequestBody CrProcessInsideDTO dto) {
    List<CrProcessInsideDTO> list = crProcessBusiness.getListCrProcessLevel3CBB(dto);
    return new ResponseEntity<>(list, HttpStatus.OK);
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
