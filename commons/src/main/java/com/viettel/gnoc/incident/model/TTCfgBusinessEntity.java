package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.TTCfgBusinessDTO;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "ONE_TM", name = "TT_CFG_BUSINESS")
public class TTCfgBusinessEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TT_CFG_BUSINESS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "TT_CHANGE_STATUS_ID")
  private Long ttChangeStatusId;

  @Column(name = "COLUMN_NAME")
  private String columnName;

  @Column(name = "IS_REQUIRED")
  private Long isRequired;

  @Column(name = "EDITABLE")
  private Long editable;

  @Column(name = "IS_VISIBLE")
  private Long isVisible;

  @Column(name = "DEFAULT_VALUE")
  private String defaultValue;

  @Column(name = "TYPE_CONTROL")
  private Long typeControl;

  @Column(name = "IS_PARENT")
  private Long isParent;

  @Column(name = "STT")
  private Long stt;

  @Column(name = "SCOPE_OF_USE")
  private Long scopeOfUse;

  @Column(name = "col_name_relate")
  private String colNameRelate;

  public TTCfgBusinessDTO toDTO() {
    return new TTCfgBusinessDTO(id, ttChangeStatusId, columnName, isRequired, editable, isVisible,
        defaultValue, typeControl, isParent, stt, scopeOfUse, colNameRelate);
  }
}
