package com.viettel.gnoc.security.proxy;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="common-stream-service")
public interface CommonProxy {

  @GetMapping("/commonStreamAPI/getSubAdminViews")
  public Map<String, String> getSubAdminViews();
}
