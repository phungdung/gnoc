package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.TroubleWireDTO;
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
@Table(schema = "ONE_TM", name = "TROUBLE_WIRE")
public class TroubleWireEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TROUBLE_WIRE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "TROUBLE_ID", nullable = false)
  private Long troubleId;

  @Column(name = "SERVICE_TYPE")
  private String serviceType;

  @Column(name = "TEL_SERVICE_ID")
  private String telServiceId;

  @Column(name = "INFRA_TYPE")
  private String infraType;

  @Column(name = "IS_CC_RESULT")
  private String isCcResult;

  @Column(name = "PRODUCT_CODE")
  private String productCode;

  @Column(name = "ACCOUNT_ISDN")
  private String accountIsdn;

  @Column(name = "CUSTOMER_PHONE")
  private String customerPhone;

  @Column(name = "CONTENT")
  private String content;

  public TroubleWireDTO toDTO() {
    return new TroubleWireDTO(id, troubleId, serviceType, telServiceId, infraType, isCcResult,
        productCode, accountIsdn, customerPhone, content);
  }
}
