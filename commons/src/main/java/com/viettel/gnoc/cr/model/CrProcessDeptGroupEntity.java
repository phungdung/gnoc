/**
 * @(#)CrProcessDeptGroupBO.java 11/16/2015 5:28 PM, Copyright 2011 Viettel Telecom. All rights
 * reserved VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CrProcessDeptGroupDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author kienpv
 * @version 1.0
 * @since 11/16/2015 5:28 PM
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "CR_PROCESS_DEPT_GROUP")
public class CrProcessDeptGroupEntity {

  //  @Id
//  @GeneratedValue(generator = "sequence")
//  @GenericGenerator(name = "sequence", strategy = "sequence",
//      parameters = {
//          @Parameter(name = "sequence", value = "cr_process_dept_group_seq")
//      }
//  )
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "open_pm.cr_process_dept_group_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CPDGP_ID", unique = true, nullable = false)
  private Long cpdgpId;

  @Column(name = "GROUP_UNIT_ID")
  private Long groupUnitId;

  @Column(name = "CR_PROCESS_ID")
  private Long crProcessId;

  @Column(name = "CPDGP_TYPE")
  private Long cpdgpType;


  public CrProcessDeptGroupDTO toDTO() {
    CrProcessDeptGroupDTO dto = new CrProcessDeptGroupDTO(cpdgpId, groupUnitId, crProcessId,
        cpdgpType
    );
    return dto;
  }
}

