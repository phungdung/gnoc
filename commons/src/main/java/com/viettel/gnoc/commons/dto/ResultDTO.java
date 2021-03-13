/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author itsol
 */
@Getter
@Setter
@NoArgsConstructor
public class ResultDTO {

  private String id;
  private String key;
  private String message;
  private int quantitySucc;
  private int quantityFail;

  private Double amount;
  private Double amountIssue;
  private String requestTime;
  private String finishTime;
  private List lstResult;
  private Long approve;
  private Long woId;

  public ResultDTO(String id, String key, String message) {
    this.id = id;
    this.key = key;
    this.message = message;
  }

  public ResultDTO(String id, String message) {
    this.id = id;
    this.message = message;
  }
}
