package com.viettel.gnoc.security.proxy;

import com.viettel.gnoc.security.dto.HazelcastDto;
import com.viettel.gnoc.security.dto.HazelcastResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="hazelcast-service")
public interface HazelcastProxy {

  @PostMapping("/hazelcast/getData")
  public HazelcastResponseDto getDataFromHazelCast(@RequestBody HazelcastDto hazelcastDto);

  @PostMapping("/hazelcast/putData")
  public HazelcastResponseDto putDataToHazelCast(@RequestBody HazelcastDto hazelcastDto);

  @PostMapping("/hazelcast/removeData")
  public HazelcastResponseDto removeDataFromHazelCast(@RequestBody HazelcastDto hazelcastDto);
}
