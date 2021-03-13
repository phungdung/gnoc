package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.sr.model.SRSearchConfigUserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SRSearchConfigUserDTO {

  private Long searchConfigUserId;
  private String functionKey;
  private String fieldValue;
  private Long userId;
  private String userName;
  private String fieldName;
  private String dataType;
  private Long indexColumn;

  public SRSearchConfigUserEntity toEntity() {
    return new SRSearchConfigUserEntity(searchConfigUserId, functionKey, fieldValue, userId,
        userName, fieldName, dataType, indexColumn);
  }
}
