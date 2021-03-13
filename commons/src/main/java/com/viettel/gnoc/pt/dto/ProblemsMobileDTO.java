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
public class ProblemsMobileDTO {

  Long newPT;//So PT cho tiep nhan
  Long rcaPT;//So PT dang tim nguyen nhan goc
  Long solutionPT;//So PT dang tim giai phap triet de
  Long outOfDatePT;//So PT qua han
}
