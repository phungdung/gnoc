package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrChecklistsBtsDetailDTO;
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
@Table(schema = "OPEN_PM", name = "MR_CHECKLISTS_BTS_DETAIL")
public class MrChecklistsBtsDetailEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_CHECKLISTS_BTS_DETAIL_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CHECKLIST_DETAIL_ID", unique = true, nullable = false)
  private Long checklistDetailId;

  @Column(name = "CHECKLIST_ID")
  private Long checklistId;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "PHOTO_REQ")
  private Long photoReq;

  @Column(name = "MIN_PHOTO")
  private Long minPhoto;

  @Column(name = "MAX_PHOTO")
  private Long maxPhoto;

  @Column(name = "CAPTURE_GUIDE")
  private String captureGuide;

  @Column(name = "SCORE_CHECKLIST")
  private Double scoreChecklist;

  @Column(name = "IS_IMPORTAINT")
  private Long isImportant;

  public MrChecklistsBtsDetailDTO toDTO() {
    return new MrChecklistsBtsDetailDTO(checklistDetailId, checklistId, content, photoReq,
        minPhoto, maxPhoto, captureGuide, scoreChecklist, isImportant);
  }

}
