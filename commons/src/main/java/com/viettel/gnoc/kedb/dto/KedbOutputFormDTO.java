package com.viettel.gnoc.kedb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KedbOutputFormDTO {

  String kedbId;
  String kedbCode;
  String kedbName;
  String kedbFileName;
  String kedbFilePath;
}
