package com.viettel.gnoc.wo.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TotalWoForm {

  private String username;
  private String key;
  private String message;
  private List<FieldForm> value;
}
