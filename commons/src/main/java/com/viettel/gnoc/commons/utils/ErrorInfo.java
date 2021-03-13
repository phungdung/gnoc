package com.viettel.gnoc.commons.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author TungPV
 */
@Getter
@Setter
@NoArgsConstructor
public class ErrorInfo {

  private int row;
  private String msg;

  public ErrorInfo(int row, String msg) {
    this.row = row;
    this.msg = msg;
  }
}
