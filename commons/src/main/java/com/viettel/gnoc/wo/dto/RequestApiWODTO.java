package com.viettel.gnoc.wo.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestApiWODTO {

  private String woCode;
  private String endPendingTime;
  private String user;
  private String userName;
  private String woId;
  private String systemId;
  private String startDate;
  private String endDate;
  private String workLog;
  private String reasonCcId;
  private Long reasonLevel3Id;
  private String reasonName;
  private String reasonId;
  private String customer;
  private String phone;
  private String comment;
  private String system;
  private Boolean callCC;
  private List<String> listCode;
  private List<String> listNode;
  private List<Wo> lstWo;
  private WoDTO createWoDto;

  public RequestApiWODTO(String userName, String woId, String system, String systemId,
      String startDate,
      String endDate) {
    this.userName = userName;
    this.woId = woId;
    this.system = system;
    this.systemId = systemId;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public RequestApiWODTO(String woCode, String userName, String workLog, String reasonCcId) {
    this.woCode = woCode;
    this.userName = userName;
    this.workLog = workLog;
    this.reasonCcId = reasonCcId;
  }

  public RequestApiWODTO(List<String> listCode, String system) {
    this.listCode = listCode;
    this.system = system;
  }

  public RequestApiWODTO(WoDTO createWoDto, List<String> listNode) {
    this.createWoDto = createWoDto;
    this.listNode = listNode;
  }

  public RequestApiWODTO(String woCode, String endPendingTime,
      String user, String comment, String system,
      Boolean callCC) {
    this.woCode = woCode;
    this.endPendingTime = endPendingTime;
    this.user = user;
    this.comment = comment;
    this.system = system;
    this.callCC = callCC;
  }

  public RequestApiWODTO(String woCode, String endPendingTime,
      String user, String system, String reasonName, String reasonId,
      String customer, String phone) {
    this.woCode = woCode;
    this.endPendingTime = endPendingTime;
    this.user = user;
    this.system = system;
    this.reasonName = reasonName;
    this.reasonId = reasonId;
    this.customer = customer;
    this.phone = phone;
  }

  public RequestApiWODTO(List<Wo> lstWo, String system, String user, Long reasonLevel3Id
  ) {
    this.lstWo = lstWo;
    this.system = system;
    this.user = user;
    this.reasonLevel3Id = reasonLevel3Id;
  }
}
