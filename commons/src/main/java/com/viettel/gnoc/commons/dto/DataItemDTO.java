/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataItemDTO {

  private String itemId;
  private String itemCode;
  private String itemName;
  private String itemValue;
  private String description;
  private String status;
  private String parenItemId;
  private String parenItemName;
}
