package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.sr.dto.SRHisDTO;
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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "SR_HIS")
public class SRHisEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_HIS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "HIS_ID")
  private Long hisId;
  @Column(name = "CREATED_USER")
  private String createdUser;
  @Column(name = "CREATED_TIME")
  private Date createdTime;
  @Column(name = "ACTION_CODE")
  private String actionCode;
  @Column(name = "SR_STATUS")
  private String srStatus;
  @Column(name = "COMMENTS")
  private String comments;
  @Column(name = "SR_ID")
  private Long srId;

  public SRHisDTO toDTO() {
    SRHisDTO dto = new SRHisDTO(
        hisId == null ? null : hisId.toString()
        , createdUser
        , createdTime == null ? null
        : DateTimeUtils.convertDateToString(createdTime, Constants.ddMMyyyyHHmmss)
        , actionCode
        , srStatus
        , comments
        , srId == null ? null : srId.toString()
    );
    return dto;
  }
}
