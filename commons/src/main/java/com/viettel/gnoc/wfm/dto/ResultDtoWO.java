package com.viettel.gnoc.wfm.dto;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author TienNV
 */
@Getter
@Setter
@NoArgsConstructor
public class ResultDtoWO {

  String id;
  String key;
  String message;
  Timestamp systemDate;
  Object object;
  File file;
  String authToken;
  String link;
  String requestTime;
  String finishTime;

  private int quantitySucc;
  private int quantityFail;
  private Double amount;
  private Double amountIssue;
  private List lstResult;

  public ResultDtoWO(String id, String key, String message) {
    this.id = id;
    this.key = key;
    this.message = message;
  }
}
