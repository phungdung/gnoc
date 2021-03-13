
package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoTypeCheckListDTO;
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
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "WFM", name = "WO_TYPE_CHECKLIST")
public class WoTypeCheckListEntity {

  static final long serialVersionUID = 1L;

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_TYPE_CHECKLIST_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WO_TYPE_CHECKLIST_ID", nullable = false)
  private Long woTypeChecklistId;

  @Column(name = "WO_TYPE_ID")
  private Long woTypeId;

  @Column(name = "CHECKLIST_NAME")
  private String checklistName;

  @Column(name = "DEFAULT_VALUE")
  private String defaultValue;

  @Column(name = "IS_ENABLE")
  private Long isEnable;


  public WoTypeCheckListDTO toDTO() {
    WoTypeCheckListDTO dto = new WoTypeCheckListDTO(
        woTypeChecklistId
        , woTypeId
        , checklistName
        , defaultValue
        , isEnable
    );
    return dto;
  }
}

