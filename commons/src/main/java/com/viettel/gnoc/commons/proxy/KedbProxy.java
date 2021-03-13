package com.viettel.gnoc.commons.proxy;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.kedb.dto.KedbDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kedb-service")
public interface KedbProxy {

  @PostMapping("/KedbService/getListKedbDTO")
  public Datatable getListKedbDTO(@RequestBody KedbDTO kedbDTO);
}
