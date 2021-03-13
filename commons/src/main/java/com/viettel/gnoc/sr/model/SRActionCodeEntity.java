package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRActionCodeDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "SR_ACTION_CODE")
public class SRActionCodeEntity {

  @Column(name = "OLD_STATUS")
  private String oldStatus;
  @Column(name = "NEW_STATUS")
  private String newStatus;
  @Column(name = "ACTION_CODE")
  private String actionCode;
  @Column(name = "DEFAULT_COMMENT")
  private String defaultComment;
  @Column(name = "CREATED_USER")
  private String createdUser;
  @Column(name = "CREATED_TIME")
  private Date createdTime;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_ACTION_CODE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "AC_ID")
  private Long acId;

  public SRActionCodeDTO toDTO() {
    return new SRActionCodeDTO(oldStatus, newStatus, actionCode, defaultComment, createdUser,
        createdTime, acId);
  }
}
