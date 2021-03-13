/**
 * @(#)CatItemForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.dto;

import java.util.Date;
import com.viettel.gnoc.commons.model.UserCommentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author itsol
 * @version 2.0
 * @since 12/03/2018
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCommentDTO extends BaseDto {

  //Fields
  private Long id;
  private String userName;
  private String content;
  private String subject;
  private Date createTime;

  private String pageStart;
  private String pageEnd;

  public UserCommentDTO( Long id, String userName, String content, String subject, Date createTime) {
    this.id = id;
    this.userName = userName;
    this.content = content;
    this.subject = subject;
    this.createTime = createTime;
  }

  public UserCommentEntity toEntity() {
    return new UserCommentEntity(id, userName, content, subject, createTime);
  }
}
