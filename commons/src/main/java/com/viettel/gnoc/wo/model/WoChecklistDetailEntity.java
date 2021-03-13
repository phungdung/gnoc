package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoChecklistDetailDTO;
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
@Table(schema = "WFM", name = "WO_CHECKLIST_DETAIL")
public class WoChecklistDetailEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_CHECKLIST_DETAIL_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WO_CHECKLIST_DETAIL_ID", unique = true, nullable = false)
  private Long woChecklistDetailId;

  @Column(name = "WO_TYPE_CHECKLIST_ID")
  private Long woTypeChecklistId;

  @Column(name = "CHECKLIST_VALUE")
  private String checklistValue;

  @Column(name = "WO_ID")
  private Long woId;

  public WoChecklistDetailDTO toDTO() {
    return new WoChecklistDetailDTO(woChecklistDetailId, woTypeChecklistId, checklistValue, woId);
  }

}
