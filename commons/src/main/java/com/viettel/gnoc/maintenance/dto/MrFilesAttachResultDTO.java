package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
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
public class MrFilesAttachResultDTO extends BaseDto {

  private String fileId;
  private String fileName;
  private String timeAttack;
  private String userId;
  private String fileType;
  private String mrId;
  private String userName;
  private String filePath;
}
