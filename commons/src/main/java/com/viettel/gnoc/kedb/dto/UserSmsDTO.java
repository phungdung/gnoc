package com.viettel.gnoc.kedb.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.model.UserSmsEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSmsDTO extends BaseDto {

  private Long userId;
  private Long smsType;
  private String typeCode;
  private Date lastUpdateTime;
  private String mobile;
  private String email;

  private String userName;
  private Long unitId;

  public UserSmsDTO(Long userId, Long smsType, String typeCode, Date lastUpdateTime,
      String mobile, String email) {
    this.userId = userId;
    this.smsType = smsType;
    this.typeCode = typeCode;
    this.lastUpdateTime = lastUpdateTime;
    this.mobile = mobile;
    this.email = email;
  }

  public UserSmsEntity toEntity() {
    return new UserSmsEntity(userId, smsType, typeCode, lastUpdateTime, email, mobile);
  }

}
