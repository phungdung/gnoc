package com.viettel.gnoc.ws.dto;

import com.viettel.gnoc.commons.dto.WoFileTempDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WoFileTempDTO {

  private String woFileTempId;
  private String woTypeId;
  private String label;
  private String numberFile;
  private String woTypeName;

  public WoFileTempDto toInsideDTO() {
    return new WoFileTempDto(
        StringUtils.isNotNullOrEmpty(woFileTempId) ? null : Long.parseLong(woFileTempId),
        StringUtils.isNotNullOrEmpty(woTypeId) ? null : Long.parseLong(woTypeId), label,
        StringUtils.isNotNullOrEmpty(numberFile) ? null : Long.parseLong(numberFile), woTypeName);
  }
}
