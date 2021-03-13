package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoHistoryInsideDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "WO_HISTORY")
public class WoHistoryEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_HISTORY_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WO_HISTORY_ID", unique = true, nullable = false)
  private Long woHistoryId;

  @Column(name = "OLD_STATUS")
  private Long oldStatus;

  @Column(name = "NEW_STATUS")
  private Long newStatus;

  @Column(name = "WO_ID")
  private Long woId;

  @Column(name = "WO_CODE")
  private String woCode;

  @Column(name = "WO_CONTENT")
  private String woContent;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "FILE_NAME")
  private String fileName;

  @Column(name = "COMMENTS")
  private String comments;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATE_TIME")
  private Date updateTime;

  @Column(name = "CREATE_MESSAGE")
  private Long createMessage;

  @Column(name = "CREATE_PERSON_ID")
  private Long createPersonId;

  @Column(name = "CD_ID")
  private Long cdId;

  @Column(name = "FT_ID")
  private Long ftId;

  @Column(name = "IS_SEND_IBM")
  private Long isSendIbm;

  @Column(name = "NATION")
  private String nation;

  public WoHistoryInsideDTO toDTO() {
    return new WoHistoryInsideDTO(woHistoryId, oldStatus, newStatus, woId, woCode, woContent,
        userId,
        userName, fileName, comments, updateTime, createMessage, createPersonId, cdId, ftId,
        isSendIbm, nation);
  }

}
