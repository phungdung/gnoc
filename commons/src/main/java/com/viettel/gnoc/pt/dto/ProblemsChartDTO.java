/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.pt.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ITSOL
 */
@Getter
@Setter
public class ProblemsChartDTO {

  Long newPT;
  Long queuePT;
  Long diagnosedPT;
  Long waFoundPT;
  Long slFoundPT;
  Long outOfDatePT;
  Long closedOutOfDatePT;
}
