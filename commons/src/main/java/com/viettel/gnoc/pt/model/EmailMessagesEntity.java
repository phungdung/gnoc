package com.viettel.gnoc.pt.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.pt.dto.EmailMessagesDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "ONE_TM", name = "EMAIL_MESSAGES")
public class EmailMessagesEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "EMAIL_MESSAGES_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "SENDER")
  private String sender;

  @Column(name = "RECEIVER")
  private String receiver;

  @Column(name = "CREATE_TIME")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date createTime;

  @Column(name = "SENT_TIME")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date sentTime;

  @Column(name = "STATUS")
  private Long status;

  @Column(name = "RESULT")
  private String result;

  @Column(name = "REPEAT")
  private Long repeat;

  @Column(name = "subject")
  private String subject;

  public EmailMessagesEntity(Long id, String content, String sender, String receiver,
      Date createTime, Date sentTime, Long status, String result, Long repeat, String subject) {
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

  public EmailMessagesDTO toDTO() {
    EmailMessagesDTO dto = new EmailMessagesDTO(
        id == null ? null : id.toString(),
        content == null ? null : content,
        sender,
        receiver,
        createTime == null ? null
            : DateTimeUtils.convertDateToString(createTime, Constants.ddMMyyyy),
        sentTime == null ? null : DateTimeUtils.convertDateToString(sentTime, Constants.ddMMyyyy),
        status == null ? null : status.toString(),
        result,
        repeat == null ? null : repeat.toString(),
        subject
    );
    return dto;
  }
}
