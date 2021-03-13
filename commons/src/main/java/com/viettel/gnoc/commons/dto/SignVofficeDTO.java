package com.viettel.gnoc.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignVofficeDTO extends BaseDto {

  private String userName;
  private String password;
  private String emailUser;
  private String title;
  private Long odId;
}
