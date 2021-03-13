package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.UserUpdateHisEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class UserUpdateHisDTO extends BaseDto {

  private Long id;
  private Long userAction;
  private Long userId;
  private String actionName;
  private String content;
  private Date updateTime;

  private String userActionName;
  private String userIdName;

  public UserUpdateHisDTO(Long id, Long userAction, Long userId, String actionName, String content, Date updateTime){
    this.id = id;
    this.userAction = userAction;
    this.userId = userId;
    this.actionName = actionName;
    this.content = content;
    this.updateTime = updateTime;
  }

  public UserUpdateHisEntity toEntity() {
    return new UserUpdateHisEntity(id, userAction, userId, actionName, content, updateTime);
  }
}
