/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CoordinationSettingDTO;
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
 * @author thanhlong
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "COORDINATION_SETTING")
public class CoordinationSettingEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.COORDINATION_SETTING_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "UNIT_ID", nullable = false)
  private Long unitId;

  @Column(name = "GROUP_ID", nullable = false)
  private Long groupId;

  public CoordinationSettingDTO toDTO() {
    CoordinationSettingDTO dto = new CoordinationSettingDTO(
        id
        , unitId
        , groupId
    );
    return dto;
  }

}
