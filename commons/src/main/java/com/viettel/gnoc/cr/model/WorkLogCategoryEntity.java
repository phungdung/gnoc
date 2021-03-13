package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
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
@Table(schema = "OPEN_PM", name = "WORK_LOG_CATEGORY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkLogCategoryEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WORK_LOG_CATEGORY_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WLAY_ID", nullable = false)
  private Long wlayId;
  @Column(name = "WLAY_TYPE", nullable = true)
  private Long wlayType;
  @Column(name = "WLAY_CODE", nullable = true)
  private String wlayCode;
  @Column(name = "WLAY_NAME", nullable = true)
  private String wlayName;
  @Column(name = "WLAY_DESCRIPTION", nullable = true)
  private String wlayDescription;
  @Column(name = "WLAY_IS_ACTIVE", nullable = true)
  private Long wlayIsActive;
  @Column(name = "WLAY_IS_EDITABLE", nullable = true)
  private Long wlayIsEditable;

  public WorkLogCategoryInsideDTO toDTO() {
    return new WorkLogCategoryInsideDTO(
        wlayId,
        wlayType,
        wlayCode,
        wlayName,
        wlayDescription,
        wlayIsActive,
        wlayIsEditable
    );
  }
}
