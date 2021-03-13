package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemplateImportDTO extends BaseDto {

  private String linkOutput;
  private String tempImportId;
  private String message;
  private String result;
  private String linkInput;
  private String linkInputFtp;
  private String crId;
  private String userId;
  private String userName;
  private String fileName;
  private String fileType;
  private byte[] bytesFileOut;
}
