package com.viettel.gnoc.hazelcast.dto;

import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HazelcastDto {
  private String key;
  private String value;
  private long expire;
  private TimeUnit timeUnit;
}
