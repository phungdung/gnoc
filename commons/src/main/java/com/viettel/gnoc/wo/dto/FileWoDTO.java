package com.viettel.gnoc.wo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileWoDTO {

  private String fileName;
  private String filePath;
  private Long woId;

}
