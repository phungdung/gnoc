package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.TTChangeStatusRoleDTO;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "ONE_TM", name = "TT_CHANGE_STATUS_ROLE")
public class TTChangeStatusRoleEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TT_CHANGE_STATUS_ROLE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true)
  private Long id;

  @Column(name = "TT_CHANGE_STATUS_ID")
  private Long ttChangeStatusId;

  @Column(name = "ROLE_ID")
  private Long roleId;

  public TTChangeStatusRoleDTO toDTO() {
    return new TTChangeStatusRoleDTO(id, ttChangeStatusId, roleId);
  }
}
