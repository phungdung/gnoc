package com.viettel.gnoc.commons.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author TungPV
 */
@Getter
@Setter
@NoArgsConstructor
public class Datatable {

  private int total;
  private int pages;
  private List<?> data;
}
