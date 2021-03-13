package com.viettel.gnoc.cr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CrFileAttachOutputWithContent {

  private String crNumber;
  private String fileName;
  private String username;
  private String filePath;
  private String fileContent;
  private String description;
  private String timeAttach;
  private String resultCode;
}
