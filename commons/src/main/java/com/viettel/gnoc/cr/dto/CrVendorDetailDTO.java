/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.cr.model.CrVendorDetailEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CrVendorDetailDTO {

  private Long cvdId;
  private Long crId;
  private String vendorCode;
  private String vendorName;
  private String createTime;

  public CrVendorDetailEntity toEntity() {
    CrVendorDetailEntity model = new CrVendorDetailEntity();
    model.setCvdId(cvdId);
    model.setCrId(crId);
    model.setVendorCode(vendorCode);
    model.setVendorName(vendorName);
    return model;
  }

}
