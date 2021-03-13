/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.aam.ModuleInfo;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AttachDtDTO {

  boolean checkbox;
  String dtCode;
  String userName;
  String createDate;
  String fileName;
  List<String> lstIpImpact;
  List<String> lstAffectService;
  List<String> lstIpAffect;
  String systemCode;//0:TDTT;1:IP;2:DD
  String nationCode;
  List<ModuleInfo> lstModule;

}
