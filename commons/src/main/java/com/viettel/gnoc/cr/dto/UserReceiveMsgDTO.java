package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.UserReceiveMsgEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class UserReceiveMsgDTO extends BaseDto {

  private Long userReceiveMsgId;
  private Long userId;
  private String userName;
  private Long crId;
  private String crNumber;
  private Date insertTime;
  private Date crCreateTime;
  private Date crStartTime;
  private Date crEndTime;

  public UserReceiveMsgEntity toEntity() {
    return new UserReceiveMsgEntity(userReceiveMsgId,
        userId, userName, crId, crNumber, insertTime,
        crCreateTime, crStartTime, crEndTime

    );
  }
}
