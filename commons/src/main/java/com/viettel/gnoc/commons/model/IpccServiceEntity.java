package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.IpccServiceDTO;
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
@Table(schema = "COMMON_GNOC", name = "IPCC_SERVICE")
public class IpccServiceEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "ipcc_service_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "IPCC_SERVICE_ID", nullable = false)
  private Long ipccServiceId;
  @Column(name = "IPCC_SERVICE_CODE")
  private String ipccServiceCode;
  @Column(name = "URL")
  private String url;
  @Column(name = "FUNCTION")
  private String function;
  @Column(name = "IS_DEFAULT")
  private Long isDefault;
  @Column(name = "USER_NAME")
  private String userName;
  @Column(name = "PASSWORD")
  private String password;

  public IpccServiceDTO toDTO() {
    return new IpccServiceDTO(ipccServiceId, ipccServiceCode, url, function, isDefault, userName,
        password);
  }

}
