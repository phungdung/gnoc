package com.viettel.gnoc.commons.proxy;

import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cr-common-business")
public interface CrCommonProxy {

  @GetMapping("/CrFilesAttachService/findFileAttachById/id{id}")
  public CrFilesAttachInsiteDTO findFileAttachById(@PathVariable(value = "id") Long id);
}
