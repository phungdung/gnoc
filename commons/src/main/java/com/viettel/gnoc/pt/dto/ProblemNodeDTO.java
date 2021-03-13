/**
 * @(#)ProblemNodeForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.pt.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.SizeByte;
import com.viettel.gnoc.pt.model.ProblemNodeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProblemNodeDTO extends BaseDto {

  private Long problemNodeId;

  private Long nodeId;

  @SizeByte(max = 100, message = "{validation.problem.node.nodecode.InvalidMaxLength}")
  private String nodeCode;
  private Long problemId;

  @SizeByte(max = 50, message = "{validation.problem.node.vendor.InvalidMaxLength}")
  private String vendor;

  @SizeByte(max = 200, message = "{validation.problem.node.nodeName.InvalidMaxLength}")
  private String nodeName;

  @SizeByte(max = 200, message = "{validation.problem.node.ip.InvalidMaxLength}")
  private String ip;

  @SizeByte(max = 100, message = "{validation.problem.node.nation.InvalidMaxLength}")
  private String nation;

  public ProblemNodeEntity toEntity() {
    return new ProblemNodeEntity(
        (problemNodeId), (nodeId), nodeCode, (problemId), vendor, nodeName, ip, nation);
  }

}
