package com.viettel.gnoc.od.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.od.dto.OdChangeStatusRoleDTO;
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
 * @author TienNV
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "OD_CHANGE_STATUS_ROLE")
public class OdChangeStatusRoleEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OD_CHANGE_STATUS_ROLE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true)
  private Long id;

  @Column(name = "OD_CHANGE_STATUS_ID")
  private Long odChangeStatusId;

  @Column(name = "ROLE_ID")
  private Long roleId;

  public OdChangeStatusRoleDTO toDTO() {
    return new OdChangeStatusRoleDTO(this.id, this.odChangeStatusId, this.roleId);
  }
}
