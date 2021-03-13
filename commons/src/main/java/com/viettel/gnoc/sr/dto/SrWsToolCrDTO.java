package com.viettel.gnoc.sr.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SrWsToolCrDTO {

  private String fileType;
  private String templateId;
  private List<SRMappingProcessCRDTO> lstSRMappingProcess;
  private List<SRConfigDTO> lstDataCountry;
  private List<SRConfigDTO> lstData;
  private String lstVmsa;
  private String lstAAM;
  private String lstVIPA;
}
