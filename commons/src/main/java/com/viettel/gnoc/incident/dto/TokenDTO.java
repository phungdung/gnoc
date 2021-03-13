package com.viettel.gnoc.incident.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class TokenDTO {

  private String access_token;
  private String token_type;
  private String expires_in;
  private String scope;
  private String jti;
}
