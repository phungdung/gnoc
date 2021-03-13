package com.viettel.gnoc.pt.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.pt.model.EmailMessagesEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class EmailMessagesDTO {

  //Fields
  private String id;
  private String content;
  private String sender;
  private String receiver;
  private String createTime;
  private String sentTime;
  private String status;
  private String result;
  private String repeat;
  private String subject;

  public EmailMessagesDTO(String id, String content, String sender, String receiver,
      String createTime, String sentTime, String status, String result, String repeat,
      String subject) {
    this.id = id;
    this.content = content;
    this.sender = sender;
    this.receiver = receiver;
    this.createTime = createTime;
    this.sentTime = sentTime;
    this.status = status;
    this.result = result;
    this.repeat = repeat;
    this.subject = subject;
  }

  public EmailMessagesEntity toEntity() {
    EmailMessagesEntity model = new EmailMessagesEntity(
        !StringUtils.validString(id) ? null : Long.valueOf(id),
        content,
        sender,
        receiver,
        !StringUtils.validString(createTime) ? null : DateTimeUtils.convertStringToDate(createTime),
        !StringUtils.validString(sentTime) ? null : DateTimeUtils.convertStringToDate(sentTime),
        !StringUtils.validString(status) ? null : Long.valueOf(status),
        result,
        !StringUtils.validString(repeat) ? null : Long.valueOf(repeat),
        subject
    );
    return model;
  }

}
