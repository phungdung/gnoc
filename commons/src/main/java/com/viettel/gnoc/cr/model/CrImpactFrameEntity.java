package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
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

@Entity
@Table(schema = "OPEN_PM", name = "CR_IMPACT_FRAME")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrImpactFrameEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CR_IMPACT_FRAME_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "IFE_ID", nullable = false)
  private Long impactFrameId;
  @Column(name = "IFE_CODE", nullable = true)
  private String impactFrameCode;
  @Column(name = "IFE_NAME", nullable = true)
  private String impactFrameName;
  @Column(name = "START_TIME", nullable = true)
  private String startTime;
  @Column(name = "END_TIME", nullable = true)
  private String endTime;
  @Column(name = "DESCRIPTION", nullable = true)
  private String description;
  @Column(name = "IS_ACTIVE", nullable = true)
  private Long isActive;
  @Column(name = "IS_EDITABLE", nullable = true)
  private Long isEditable;
  @Column(name = "CREATED_TIME", nullable = true)
  private Date createdTime;
  @Column(name = "UPDATED_TIME", nullable = true)
  private Date updatedTime;
  @Column(name = "CREATED_USER", nullable = true)
  private String createdUser;
  @Column(name = "UPDATED_USER", nullable = true)
  private String updatedUser;

  public CrImpactFrameInsiteDTO toDTO() {
    return new CrImpactFrameInsiteDTO(
        impactFrameId,
        impactFrameCode,
        impactFrameName,
        startTime,
        endTime,
        description,
        isActive,
        isEditable,
        createdTime,
        updatedTime,
        createdUser,
        updatedUser
    );
  }
}
