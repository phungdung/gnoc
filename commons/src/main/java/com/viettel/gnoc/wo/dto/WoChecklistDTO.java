/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ITSOL
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoChecklistDTO {

  private Long woId;
  private Long woTypeId;
  private Long woTypeChecklistId;
  private String checklistName;
  private String defaultValue;
  private Long woChecklistDetailId;
  private String checklistValue;

}
