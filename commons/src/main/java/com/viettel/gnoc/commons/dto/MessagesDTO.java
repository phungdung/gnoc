package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.MessagesCommonEntity;
import com.viettel.gnoc.commons.model.MessagesWfmEntity;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ITSOL
 */

@Slf4j
@Setter
@Getter
@NoArgsConstructor
public class MessagesDTO extends BaseDto {

  //Fields
  private String messageId;
  private String smsGatewayId;
  private String content;
  private String senderId;
  private String receiverId;
  private String receiverUsername;
  private String receiverFullName;
  private String createTime;
  private String sentTime;
  private String status;
  private String result;
  private String repeat;
  private String receiverPhone;
  private String alias;
  private String userLanguage;

  public MessagesDTO(String messageId, String smsGatewayId, String content, String senderId,
      String receiverId, String receiverUsername, String receiverFullName, String createTime,
      String sentTime, String status, String result, String repeat, String receiverPhone,
      String alias) {
    this.messageId = messageId;
    this.smsGatewayId = smsGatewayId;
    this.content = content;
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.receiverUsername = receiverUsername;
    this.receiverFullName = receiverFullName;
    this.createTime = createTime;
    this.sentTime = sentTime;
    this.status = status;
    this.result = result;
    this.repeat = repeat;
    this.receiverPhone = receiverPhone;
    this.alias = alias;
  }

  public MessagesCommonEntity toCommonEntity() {
    MessagesCommonEntity model = new MessagesCommonEntity(
        !StringUtils.validString(messageId) ? null : Long.valueOf(messageId),
        !StringUtils.validString(smsGatewayId) ? null : Long.valueOf(smsGatewayId),
        content,
        !StringUtils.validString(senderId) ? null : Long.valueOf(senderId),
        !StringUtils.validString(receiverId) ? null : Long.valueOf(receiverId),
        receiverUsername,
        receiverFullName,
        StringUtils.validString(createTime) ? DateTimeUtils.convertStringToDate(createTime)
            : null,
        StringUtils.validString(sentTime) ? DateTimeUtils.convertStringToDate(sentTime) : null,
        !StringUtils.validString(status) ? null : Long.valueOf(status),
        result,
        !StringUtils.validString(repeat) ? null : Long.valueOf(repeat),
        receiverPhone,
        alias
    );
    return model;
  }

  public MessagesWfmEntity toWfmEntity() {
    MessagesWfmEntity model = new MessagesWfmEntity(
        !StringUtils.validString(messageId) ? null : Long.valueOf(messageId),
        !StringUtils.validString(smsGatewayId) ? null : Long.valueOf(smsGatewayId),
        content,
        !StringUtils.validString(senderId) ? null : Long.valueOf(senderId),
        !StringUtils.validString(receiverId) ? null : Long.valueOf(receiverId),
        receiverUsername,
        receiverFullName,
        StringUtils.validString(createTime) ? DateTimeUtils.convertStringToDate(createTime)
            : null,
        StringUtils.validString(sentTime) ? DateTimeUtils.convertStringToDate(sentTime) : null,
        !StringUtils.validString(status) ? null : Long.valueOf(status),
        result,
        !StringUtils.validString(repeat) ? null : Long.valueOf(repeat),
        receiverPhone,
        alias
    );
    return model;
  }
}
