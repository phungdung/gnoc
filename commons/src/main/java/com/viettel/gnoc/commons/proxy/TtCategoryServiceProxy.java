package com.viettel.gnoc.commons.proxy;

import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import com.viettel.gnoc.incident.dto.TTChangeStatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "tt-category-service")
public interface TtCategoryServiceProxy {

  @PostMapping("/CfgTimeTroubleProcess/getConfigTimeTroubleProcess")
  public CfgTimeTroubleProcessDTO getConfigTimeTroubleProcess(
      @RequestBody CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO);

  @PostMapping("/TTChangeStatus/getDetailCfg")
  public TTChangeStatusDTO getDetailCfg(
      @RequestBody TTChangeStatusDTO ttChangeStatusDTO);
}
