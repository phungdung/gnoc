/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ITSOL
 */
@Getter
@Setter
public class ExcelSheetDTO {

  private String sheetName;
  private List<String> titles;
  private List<List<String>> data;

}
