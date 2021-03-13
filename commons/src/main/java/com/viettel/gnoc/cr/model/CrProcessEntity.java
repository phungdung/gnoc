package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
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

/**
 * @author KienPV
 */
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "OPEN_PM", name = "CR_PROCESS")
public class CrProcessEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "open_pm.CR_PROCESS_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CR_PROCESS_ID", unique = true, nullable = false)
  private Long crProcessId;

  @Column(name = "CR_PROCESS_CODE")
  private String crProcessCode;

  @Column(name = "CR_PROCESS_NAME")
  private String crProcessName;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "IMPACT_SEGMENT_ID")
  private Long impactSegmentId;

  @Column(name = "DEVICE_TYPE_ID")
  private Long deviceTypeId;

  @Column(name = "SUBCATEGORY_ID")
  private Long subcategoryId;

  @Column(name = "RISK_LEVEL")
  private Long riskLevel;

  @Column(name = "IMPACT_TYPE")
  private Long impactType;

  @Column(name = "CR_TYPE_ID")
  private Long crTypeId;

  @Column(name = "IS_ACTIVE")
  private Long isActive;

  @Column(name = "PARENT_ID")
  private Long parentId;

  @Column(name = "IMPACT_CHARACTERISTIC")
  private Long impactCharacteristic;

  @Column(name = "OTHER_DEPT")
  private String otherDept;

  @Column(name = "REQUIRE_MOP")
  private Long requireMop;

  @Column(name = "REQUIRE_FILE_LOG")
  private Long requireFileLog;

  @Column(name = "REQUIRE_FILE_TEST")
  private Long requireFileTest;

  @Column(name = "APPROVAL_LEVEL")
  private Long approvalLevel;

  @Column(name = "CLOSE_CR_WHEN_RESOLVE_SUCCESS")
  private Long closeCrWhenResolveSuccess;

  @Column(name = "IS_VMSA_ACTIVE_CELL_PROCESS")
  private Long isVmsaActiveCellProcess;

  @Column(name = "VMSA_ACTIVE_CELL_PROCESS_KEY")
  private String vmsaActiveCellProcessKey;

  @Column(name = "IS_LANE_IMPACT")
  private Long isLaneImpact;

  @Column(name = "REQUIRE_APPROVE")
  private Long requireApprove;

  @Column(name = "CR_PROCESS_INDEX")
  private Long crProcessIndex;

  public CrProcessInsideDTO toDTO() {
    return new CrProcessInsideDTO(crProcessId, crProcessCode, crProcessName, description,
        impactSegmentId,
        deviceTypeId, subcategoryId, riskLevel, impactType, crTypeId,
        isActive, parentId, impactCharacteristic, otherDept, requireMop, requireFileLog,
        requireFileTest,
        approvalLevel, closeCrWhenResolveSuccess, isVmsaActiveCellProcess, vmsaActiveCellProcessKey,
        isLaneImpact, requireApprove, crProcessIndex);
  }
}
