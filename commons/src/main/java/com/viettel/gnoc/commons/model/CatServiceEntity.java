
package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.CatServiceDTO;
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
@Table(schema = "COMMON_GNOC", name = "CAT_SERVICE")
public class CatServiceEntity {

  static final long serialVersionUID = 1L;

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CAT_SERVICE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "SERVICE_ID", nullable = false)
  private Long serviceId;

  @Column(name = "SERVICE_CODE")
  private String serviceCode;

  @Column(name = "SERVICE_NAME")
  private String serviceName;

  @Column(name = "SERVICE_CODE_CC1")
  private String serviceCodeCc1;

  @Column(name = "SERVICE_CODE_CC2")
  private String serviceCodeCc2;

  @Column(name = "IS_CHECK_QOS_INTERNET")
  private Long isCheckQosInternet;

  @Column(name = "IS_CHECK_QOS_TH")
  private Long isCheckQosTh;

  @Column(name = "IS_CHECK_QR_CODE")
  private Long isCheckQrCode;

  @Column(name = "IS_ENABLE")
  private Long isEnable;


  public CatServiceEntity(String serviceCode, String serviceName, String serviceCodeCc1,
      String serviceCodeCc2, Long isCheckQosInternet, Long isCheckQosTh, Long isCheckQrCode,
      Long isEnable) {
    this.serviceCode = serviceCode;
    this.serviceName = serviceName;
    this.serviceCodeCc1 = serviceCodeCc1;
    this.serviceCodeCc2 = serviceCodeCc2;
    this.isCheckQosInternet = isCheckQosInternet;
    this.isCheckQosTh = isCheckQosTh;
    this.isCheckQrCode = isCheckQrCode;
    this.isEnable = isEnable;
  }

  public CatServiceDTO toDTO() {
    CatServiceDTO dto = new CatServiceDTO(
        serviceId
        , serviceCode
        , serviceName
        , serviceCodeCc1
        , serviceCodeCc2
        , isCheckQosInternet
        , isCheckQosTh
        , isCheckQrCode
        , isEnable
    );
    return dto;
  }
}

