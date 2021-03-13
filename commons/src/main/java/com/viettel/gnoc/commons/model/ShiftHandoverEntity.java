package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.ShiftHandoverDTO;
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

@Entity
@Table(schema = "COMMON_GNOC", name = "SHIFT_HANDOVER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShiftHandoverEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SHIFT_HANDOVER_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;
  @Column(name = "USER_NAME", nullable = false)
  private String userName;
  @Column(name = "USER_ID")
  private Long userId;
  @Column(name = "UNIT_NAME")
  private String unitName;
  @Column(name = "UNIT_ID")
  private Long unitId;
  @Column(name = "SHIFT_ID")
  private Long shiftId;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_TIME")
  private Date lastUpdateTime;
  @Column(name = "STATUS")
  private Long status;
  @Column(name = "CREATED_TIME")
  private Date createdTime;

  public ShiftHandoverDTO toDTO() {
    return new ShiftHandoverDTO(id, userName, userId, unitName, unitId, shiftId, lastUpdateTime,
        status, createdTime);
  }
}
