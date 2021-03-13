/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author tiennv
 */
@Getter
@Setter
@NoArgsConstructor
public class MessageForm {

  private Long userId;
  private String userName;
  private String roleCode;
  private String roleName;
  private String deptName;
  private String deptCode;
  private Long isCommitte;
  private Long deptId;
  private Long smsGatewayId;
  private String cellPhone;
  private String email;
  private String messageContent;
  private String userLanguage;
  private Long cmseId;
  private Long unitId;
  private String messConclusion;
  private boolean saveNotify;
  private Long actionCode;
  private String alias;

}
