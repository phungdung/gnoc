/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.aam.AppGroup;
import com.viettel.gnoc.commons.dto.BaseDto;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AppGroupInsite extends BaseDto {

  List<AppGroup> lstAppGroup;

}
