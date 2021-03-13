package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SRSearchDTO extends BaseDto {

  private List<SRMappingProcessCRDTO> lstProcess; // quy trinh
  private List<SRCatalogDTO> lstService;
}
