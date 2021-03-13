package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.UserReceiveMsgDTO;
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

@Entity
@Table(schema = "OPEN_PM", name = "USER_RECEIVE_MSG")
@Getter
@Setter
@NoArgsConstructor
public class UserReceiveMsgEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "user_receive_msg_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "USER_RECEIVE_MSG_ID", nullable = false)
  private Long userReceiveMsgId;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "CR_ID")
  private Long crId;

  @Column(name = "CR_NUMBER")
  private String crNumber;

  @Column(name = "INSERT_TIME")
  @Temporal(TemporalType.TIMESTAMP)
  private Date insertTime;

  @Column(name = "CR_CREATE_TIME")
  @Temporal(TemporalType.TIMESTAMP)
  private Date crCreateTime;

  @Column(name = "CR_START_TIME")
  @Temporal(TemporalType.TIMESTAMP)
  private Date crStartTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CR_END_TIME")
  private Date crEndTime;

  public UserReceiveMsgEntity(Long userReceiveMsgId, Long userId, String userName, Long crId,
      String crNumber, Date insertTime, Date crCreateTime, Date crStartTime, Date crEndTime) {
    this.userReceiveMsgId = userReceiveMsgId;
    this.userId = userId;
    this.userName = userName;
    this.crId = crId;
    this.crNumber = crNumber;
    this.insertTime = insertTime;
    this.crCreateTime = crCreateTime;
    this.crStartTime = crStartTime;
    this.crEndTime = crEndTime;
  }

  public UserReceiveMsgDTO toDTO() {
    UserReceiveMsgDTO dto = new UserReceiveMsgDTO(
        userReceiveMsgId,
        userId,
        userName,
        crId,
        crNumber,
        insertTime,
        crCreateTime,
        crStartTime,
        crEndTime
    );
    return dto;
  }
}
