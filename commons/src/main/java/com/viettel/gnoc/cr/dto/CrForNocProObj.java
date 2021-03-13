package com.viettel.gnoc.cr.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrForNocProObj {

  private List<CrForNocProDTO> dataList = new ArrayList<>();
  private List<CrDetailInfoDTO> crDetailList = new ArrayList<>();
  private Integer status;
  private String description;
  private String key;

}
