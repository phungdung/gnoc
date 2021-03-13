package com.viettel.gnoc.commons.proxy;

import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kedb-common-business")
public interface KedbServiceProxy {

  @PostMapping("/KedbService/getOffset")
  public String getOffset(@RequestBody UserTokenGNOCSimple userTokenGNOC);
}
