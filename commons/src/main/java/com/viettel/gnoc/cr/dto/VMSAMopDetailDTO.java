/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VMSAMopDetailDTO {

  private String mopFileContent;
  private String mopFileName;
  private String mopFileType;
  private String mopId;
  private String mopName;
  private List<String> ipList = new ArrayList<>();
  private String createTime;
  private List<String> affectServices;

}
