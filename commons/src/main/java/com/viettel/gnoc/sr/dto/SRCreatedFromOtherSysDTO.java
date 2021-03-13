package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.sr.model.SRCreatedFromOtherSysEntity;
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
public class SRCreatedFromOtherSysDTO {

  private String system;
  private String subOrderId;
  private String serviceName;
  private String content;
  private String customer;
  private String address;
  private String accountSipTrunk;
  private String ipPbx;
  private String ipProxy;
  private String subscribers;
  private Long calls;
  private String srCode;
  private String systemName;
  private Long objectId;
  private String defaultSortField;

  public SRCreatedFromOtherSysDTO(String system, String subOrderId, String serviceName,
      String content, String customer, String address, String accountSipTrunk, String ipPbx,
      String ipProxy, String subscribers, Long calls, String srCode, String systemName,
      Long objectId) {
    this.system = system;
    this.subOrderId = subOrderId;
    this.serviceName = serviceName;
    this.content = content;
    this.customer = customer;
    this.address = address;
    this.accountSipTrunk = accountSipTrunk;
    this.ipPbx = ipPbx;
    this.ipProxy = ipProxy;
    this.subscribers = subscribers;
    this.calls = calls;
    this.srCode = srCode;
    this.systemName = systemName;
    this.objectId = objectId;
  }

  public SRCreatedFromOtherSysEntity toEntity() {
    SRCreatedFromOtherSysEntity entity = new SRCreatedFromOtherSysEntity(
        system == null ? null : Long.valueOf(system)
        , subOrderId == null ? null : Long.valueOf(subOrderId)
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
    return entity;
  }
}
