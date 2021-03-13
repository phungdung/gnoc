package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CrCabUsersDTO;
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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "OPEN_PM", name = "CR_CAB_USERS")
public class CrCabUsersEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CR_CAB_USERS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CR_CAB_USERS_ID", nullable = false)
  private Long crCabUsersId;
  @Column(name = "IMPACT_SEGMENT_ID")
  private Long impactSegmentId;
  @Column(name = "EXECUTE_UNIT_ID")
  private Long executeUnitId;
  @Column(name = "CAB_UNIT_ID")
  private Long cabUnitId;
  @Column(name = "USER_ID")
  private Long userID;
  @Column(name = "CREATION_UNIT_ID")
  private Long creationUnitId;

  public CrCabUsersDTO toDTO() {
    return new CrCabUsersDTO(crCabUsersId, impactSegmentId, executeUnitId, cabUnitId, userID,
        creationUnitId);
  }
}
