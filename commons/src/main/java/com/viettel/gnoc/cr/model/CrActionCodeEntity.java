package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CrActionCodeDTO;
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
@Table(schema = "OPEN_PM", name = "CR_ACTION_CODE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrActionCodeEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CR_ACTION_CODE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CR_ACTION_CODE_ID", nullable = false)
  private Long crActionCodeId;
  @Column(name = "CR_ACTION_CODE", nullable = true)
  private String crActionCode;
  @Column(name = "CR_ACTION_CODE_TITTLE", nullable = true)
  private String crActionCodeTittle;
  @Column(name = "IS_ACTIVE", nullable = true)
  private Long isActive;
  @Column(name = "IS_EDITABLE", nullable = true)
  private Long isEditable;

  public CrActionCodeDTO toDTO() {
    CrActionCodeDTO dto = new CrActionCodeDTO(
        crActionCodeId,
        crActionCode,
        crActionCodeTittle,
        isActive,
        isEditable
    );
    return dto;
  }
}
