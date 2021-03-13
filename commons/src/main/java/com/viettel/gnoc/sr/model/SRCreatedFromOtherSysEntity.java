package com.viettel.gnoc.sr.model;

import com.viettel.gnoc.sr.dto.SRCreatedFromOtherSysDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Entity
@Table(schema = "OPEN_PM", name = "SR_CREATED_FROM_OTHER_SYS")
public class SRCreatedFromOtherSysEntity {

  @Column(name = "SYSTEM")
  private Long system;
  @Id
  @Column(name = "SUB_ORDER_ID")
  private Long subOrderId;

  @Column(name = "SERVICE_NAME")
  private String serviceName;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "CUSTOMER")
  private String customer;

  @Column(name = "ADDRESS")
  private String address;

  @Column(name = "ACCOUNT_SIP_TRUNK")
  private String accountSipTrunk;

  @Column(name = "IP_PBX")
  private String ipPbx;

  @Column(name = "IP_PROXY")
  private String ipProxy;

  @Column(name = "SUBSCRIBERS")
  private String subscribers;

  @Column(name = "CALLS")
  private Long calls;

  @Column(name = "SR_CODE")
  private String srCode;

  @Column(name = "SYSTEM_NAME")
  private String systemName;

  @Column(name = "OBJECT_ID")
  private Long objectId;

  public SRCreatedFromOtherSysDTO toDTO() {
    SRCreatedFromOtherSysDTO dto = new SRCreatedFromOtherSysDTO(
        system == null ? null : system.toString()
        , subOrderId == null ? null : subOrderId.toString()
        , serviceName
        , content
        , customer
        , address
        , accountSipTrunk
        , ipPbx
        , ipProxy
        , subscribers
        , calls
        , srCode
        , systemName
        , objectId
    );
    return dto;
  }
}
