package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrChecklistsBtsDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "OPEN_PM", name = "MR_CHECKLISTS_BTS")
public class MrChecklistsBtsEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_CHECKLISTS_BTS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CHECKLIST_ID", unique = true, nullable = false)
  private Long checklistId;

  @Column(name = "MARKET_CODE")
  private String marketCode;

  @Column(name = "ARRAY_CODE")
  private String arrayCode;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "MATERIAL_TYPE")
  private String materialType;

  @Column(name = "CREATED_USER")
  private String createdUser;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "UPDATED_USER")
  private String updatedUser;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATED_TIME")
  private Date updatedTime;

  @Column(name = "CYCLE")
  private Long cycle;

  @Column(name = "SUPPLIER_CODE")
  private String supplierCode;

  @Column(name = "IMES_CHECK")
  private Long imesCheck;

  public MrChecklistsBtsDTO toDTO() {
    return new MrChecklistsBtsDTO(checklistId, marketCode, arrayCode, deviceType, materialType,
        createdUser, createdTime, updatedUser, updatedTime, cycle, supplierCode, imesCheck);
  }
}
