package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.TableConfigUserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableConfigUserDTO extends BaseDto {

  //Fields
  private Long tableConfigUserId;
  private Long userId;
  private String userName;
  private String headerKey;
  private String note;
  private String headerConfig;

  public TableConfigUserEntity toEntity() {
    return new TableConfigUserEntity(tableConfigUserId, userId, userName, headerKey, note,
        headerConfig);
  }

}
