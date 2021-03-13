/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author tuanpv14
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeZoneDTO {

  String gnocTimezoneId;
  String timezoneName;
  String nationCode;
}
