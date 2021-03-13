package com.viettel.gnoc.commons.proxy;

import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sr-service")
public interface SrServiceProxy {

  @PostMapping("/SR/getListSRForWO")
  public List<SRDTO> getListSRForWO(@RequestBody SRDTO srdto);

  @PostMapping("/SR/getListSRForLinkCR/loginUser{loginUser}/srCode{srCode}")
  public List<SRDTO> getListSRForLinkCR(@PathVariable(value = "loginUser") String loginUser,
      @PathVariable(value = "srCode") String srCode);

  @PostMapping("/getListSRCatalogByConfigGroup/configGroup{configGroup}")
  public List<SRCatalogDTO> getListSRCatalogByConfigGroup(
      @PathVariable(value = "configGroup") String configGroup);

  @PostMapping("/SR/getListDataSearchForPt")
  public List<SrInsiteDTO> getListDataSearchForPt(@RequestBody SrInsiteDTO srInsiteDTO);

  @GetMapping("/SR/findSrFromOdByProxyId{srId}")
  public SrInsiteDTO findSrFromOdByProxyId(@PathVariable(value = "srId") Long srId);

}
