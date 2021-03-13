/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.maintenance.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ITSOL
 */
@Getter
@Setter
@NoArgsConstructor
public class MrClientDetail {

  private List<MrClientItem> lstState;
  private List<MrClientItem> lstStateOutDate;
}
