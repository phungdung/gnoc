package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.CompCauseDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoCompleteDTO {

  private List<WoDTOSearchWeb> list;
  private List<CompCauseDTO> causeDTOList;
  private String comment;
}
