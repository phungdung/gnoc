package com.viettel.gnoc.commons.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "authen-service")
public interface AuthenticateProxy {

  @GetMapping("/oauthVsa/getUserFromCache/ticket{ticket}")
  public String getUserFromCache(@PathVariable(value = "ticket") String ticket);
}
