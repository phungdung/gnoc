package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRConfig2DTO;
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

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "SR_CONFIG2")
public class SRConfig2Entity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_CONFIG_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CONFIG_ID")
  private Long configId;
  @Column(name = "CONFIG_CODE")
  private String configCode;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "NEXT_STATUS")
  private String nextStatus;
  @Column(name = "SERVICE_ARRAY")
  private String serviceArray;
  @Column(name = "SERVICE_GROUP")
  private String serviceGroup;
  @Column(name = "CURRENT_STATUS")
  private String currentStatus;
  @Column(name = "CONFIG_GROUP")
  private String configGroup;
  @Column(name = "CONFIG_DES")
  private String configDes;
  @Column(name = "SERVICE_CODE")
  private String serviceCode;

  public SRConfig2DTO toDTO() {
    return new SRConfig2DTO(configId, configCode, status, nextStatus, serviceArray, serviceGroup,
        currentStatus, configGroup, configDes, serviceCode);
  }
}
