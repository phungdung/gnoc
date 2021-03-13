package com.viettel.gnoc.od.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.od.dto.OdCfgBusinessDTO;
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
@Table(schema = "WFM", name = "OD_CFG_BUSINESS")
public class OdCfgBusinessEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OD_CFG_BUSINESS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "OD_CHANGE_STATUS_ID")
  private Long odChangeStatusId;

  @Column(name = "COLUMN_NAME")
  private String columnName;

  @Column(name = "IS_REQUIRED")
  private Long isRequired;

  @Column(name = "EDITABLE")
  private Long editable;

  @Column(name = "IS_VISIBLE")
  private Long isVisible;

  @Column(name = "MESSAGE")
  private String message;

  public OdCfgBusinessDTO toDTO() {
    return new OdCfgBusinessDTO(id, odChangeStatusId, columnName, isRequired, editable, isVisible,
        message);
  }
}
