package com.viettel.gnoc.kedb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDTO {

  private String requestId;
  private String username;
  private String password;
  private String partyCode;
}
