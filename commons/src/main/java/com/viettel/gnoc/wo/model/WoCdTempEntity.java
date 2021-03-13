package com.viettel.gnoc.wo.model;

import com.viettel.gnoc.wo.dto.WoCdTempInsideDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "WO_CD_TEMP")
public class WoCdTempEntity {

  @Id
  @Column(name = "WO_CD_TEMP_ID")
  private Long woCdTempId;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "WO_GROUP_ID")
  private Long woGroupId;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "START_TIME")
  private Date startTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "END_TIME")
  private Date endTime;

  @Column(name = "IS_CD")
  private Long isCd;

  @Column(name = "STATUS")
  private Long status;

  public WoCdTempInsideDTO toDTO() {
    return new WoCdTempInsideDTO(woCdTempId, userId, woGroupId, startTime, endTime, isCd, status);
  }

}
