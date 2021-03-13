package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.WoFileTempEntity;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.ws.dto.WoFileTempDTO;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@MultiFieldUnique(message = "{validation.srRole.null.unique}", clazz = WoFileTempEntity.class, uniqueFields = "woTypeId,", idField = "woFileTempId")
public class WoFileTempDto extends BaseDto {

  private Long woFileTempId;
  @NotNull(message = "{validation.WoType.null}")
  private Long woTypeId;
  @NotEmpty(message = "{validation.WoFileTempDto.null.label}")
  private String label;
  @NotNull(message = "{validation.WoFileTempDto.null.numberFile}")
  private Long numberFile;
  private String woTypeName;


  public WoFileTempDto(Long woFileTempId, Long woTypeId, String label,
      Long numberFile) {
    this.woFileTempId = woFileTempId;
    this.woTypeId = woTypeId;
    this.label = label;
    this.numberFile = numberFile;
  }

  public WoFileTempEntity toEntity() {
    return new WoFileTempEntity(woFileTempId, woTypeId, label, numberFile);
  }

  public WoFileTempDTO toOutsideDTO() {
    return new WoFileTempDTO(
        StringUtils.isLongNullOrEmpty(woFileTempId) ? null : woFileTempId.toString(),
        StringUtils.isLongNullOrEmpty(woTypeId) ? null : woTypeId.toString(), label,
        StringUtils.isLongNullOrEmpty(numberFile) ? null : numberFile.toString(), woTypeName);
  }
}
