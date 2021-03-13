/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.cr.dto.CrModuleDetailDTO;
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

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "OPEN_PM", name = "CR_MODULE_DETAIL")
public class CrModuleDetailEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CR_MODULE_DETAIL_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CMD_ID", unique = true, nullable = false)
  private Long cmdId;

  @Column(name = "CR_ID")
  private Long crId;

  @Column(name = "SERVICE_CODE")
  private String serviceCode;

  @Column(name = "SERVICE_NAME")
  private String serviceName;

  @Column(name = "MODULE_CODE")
  private String moduleCode;

  @Column(name = "MODULE_NAME")
  private String moduleName;

  @Column(name = "NATION_CODE")
  private String nationCode;

  @Column(name = "CREATE_TIME")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date createTime;

  public CrModuleDetailDTO toDTO() {
    CrModuleDetailDTO dTO = new CrModuleDetailDTO();
    dTO.setCmdId(cmdId);
    dTO.setCrId(crId);
    dTO.setModuleCode(moduleCode);
    dTO.setModuleName(moduleName);
    dTO.setServiceCode(serviceCode);
    dTO.setServiceName(serviceName);
    dTO.setNationCode(nationCode);
    dTO.setCreateTime(createTime == null ? "" : DateTimeUtils.date2ddMMyyyyHHMMss(createTime));
    return dTO;
  }

}
