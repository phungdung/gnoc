package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoTypeServiceInsideDTO;
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
@Table(schema = "WFM", name = "WO_TYPE_SERVICE")
public class WoTypeServiceEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_TYPE_SERVICE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WO_TYPE_SERVICE_ID", unique = true, nullable = false)
  private Long woTypeServiceId;

  @Column(name = "WO_TYPE_ID")
  private Long woTypeId;

  @Column(name = "SERVICE_ID")
  private Long serviceId;

  @Column(name = "IS_CHECK_QOS_INTERNET")
  private Long isCheckQosInternet;

  @Column(name = "IS_CHECK_QOS_TH")
  private Long isCheckQosTh;

  @Column(name = "IS_CHECK_QR_CODE")
  private Long isCheckQrCode;

  public WoTypeServiceInsideDTO toDTO() {
    return new WoTypeServiceInsideDTO(woTypeServiceId, woTypeId, serviceId, isCheckQosInternet,
        isCheckQosTh, isCheckQrCode);
  }

}
