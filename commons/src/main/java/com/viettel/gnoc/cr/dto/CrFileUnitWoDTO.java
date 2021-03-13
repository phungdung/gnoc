package com.viettel.gnoc.cr.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class CrFileUnitWoDTO {

  private CrProcessInsideDTO crProcessDTO;
  private List<CrProcessTemplateDTO> listCrProcess;
  private List<CrProcessDeptGroupDTO> listCrProcessDeptGroup;
  private List<CrProcessWoDTO> listCrProcessWo;
}
