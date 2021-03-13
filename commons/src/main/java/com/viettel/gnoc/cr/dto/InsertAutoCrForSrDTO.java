package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.wo.dto.WoDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class InsertAutoCrForSrDTO {

  private CrDTO crDTO;
  private List<CrFilesAttachDTO> lstFile;
  private String system;
  private String nationCode;
  private List<WoDTO> lstWo;
  private List<String> lstMop;
  private List<String> lstNodeIp;
  private String locale;
}
