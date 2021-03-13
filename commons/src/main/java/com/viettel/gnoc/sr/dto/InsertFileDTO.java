package com.viettel.gnoc.sr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InsertFileDTO {

  private String userService;
  private String passService;
  private String userName;
  private String crNumber;
  private String fileType;
  private String fileName;
  private String fileContent;
}
