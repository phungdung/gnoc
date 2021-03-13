/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.cr.dto.CrVendorDetailDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "OPEN_PM", name = "CR_VENDOR_DETAIL")
public class CrVendorDetailEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CR_VENDOR_DETAIL_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CVD_ID", nullable = false)
  private Long cvdId;

  @Column(name = "CR_ID")
  private Long crId;

  @Column(name = "VENDOR_CODE")
  private String vendorCode;

  @Column(name = "VENDOR_NAME")
  private String vendorName;

  @Column(name = "CREATE_TIME")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date createTime;

  public CrVendorDetailDTO toDTO() {
    CrVendorDetailDTO model = new CrVendorDetailDTO();
    model.setCvdId(cvdId);
    model.setCrId(crId);
    model.setVendorCode(vendorCode);
    model.setVendorName(vendorName);
    model.setCreateTime(createTime == null ? "" : DateUtil.date2ddMMyyyyHHMMss(createTime));
    return model;
  }

}
