package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author ITSOL
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(schema = "WFM", name = "MESSAGES")
public class MessagesWfmEntity {

  static final long serialVersionUID = 1L;

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "common_gnoc.messages_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "MESSAGE_ID", nullable = false)
  private Long messageId;

  @Column(name = "SMS_GATEWAY_ID")
  private Long smsGatewayId;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "SENDER_ID")
  private Long senderId;

  @Column(name = "RECEIVER_ID")
  private Long receiverId;

  @Column(name = "RECEIVER_USERNAME")
  private String receiverUsername;

  @Column(name = "RECEIVER_FULL_NAME")
  private String receiverFullName;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATE_TIME")
  private Date createTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "SENT_TIME")
  private Date sentTime;

  @Column(name = "STATUS", nullable = false)
  private Long status;

  @Column(name = "RESULT")
  private String result;

  @Column(name = "REPEAT")
  private Long repeat;

  @Column(name = "RECEIVER_PHONE")
  private String receiverPhone;

  @Column(name = "ALIAS")
  private String alias;

  public MessagesWfmEntity(Long messageId, Long smsGatewayId, String content, Long senderId,
      Long receiverId,
      String receiverUsername, String receiverFullName, Date createTime, Date sentTime, Long status,
      String result, Long repeat, String receiverPhone, String alias) {
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

  public MessagesDTO toDTO() {
    MessagesDTO dto = new MessagesDTO(
        messageId == null ? null : messageId.toString(),
        smsGatewayId == null ? null : smsGatewayId.toString(),
        content,
        senderId == null ? null : senderId.toString(),
        receiverId == null ? null : receiverId.toString(),
        receiverUsername,
        receiverFullName,
        createTime == null ? null : DateTimeUtils.convertDateToString(createTime//
            , Constants.formatterDateTimeText),
        sentTime == null ? null : DateTimeUtils.convertDateToString(sentTime//
            , Constants.formatterDateTimeText),
        status == null ? null : status.toString(),
        result,
        repeat == null ? null : repeat.toString(),
        receiverPhone,
        alias
    );
    return dto;
  }
}
