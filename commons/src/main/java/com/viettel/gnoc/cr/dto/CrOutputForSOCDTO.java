package com.viettel.gnoc.cr.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CrOutputForSOCDTO {

  private List<String> lstAffectedService;
  private String crNumber;
  private String title;
  private String description;
  private List<Node> lstNodeName;
  private String notes;
  private String impactService;
  private String startTime;
  private String endTime;
  private String startAffectedTime;
  private String endAffectedTime;
  private String unitExecuteName;
  private String createUserName;
  private String executeUserName;
  private String lastUpdateTime;
}
