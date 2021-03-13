package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.UserImpactSegmentDTO;
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
@Table(schema = "COMMON_GNOC", name = "USER_IMPACT_SEGMENT")
public class UserImpactSegmentEntity {

  static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "user_impact_segment_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "id", unique = true)
  private Long id;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "IMPACT_SEGMENT_ID")
  private Long impactSegmentId;

  public UserImpactSegmentDTO toDTO() {
    UserImpactSegmentDTO dto = new UserImpactSegmentDTO(
        id == null ? null : id.toString(),
        userId == null ? null : userId.toString(),
        impactSegmentId == null ? null : impactSegmentId.toString()
    );
    return dto;
  }
}
