package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.CfgRoleDataDTO;
import java.util.Date;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "CFG_ROLE_DATA")
public class CfgRoleDataEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CFG_ROLE_DATA_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "USERNAME", unique = true)
  private String username;

  @Column(name = "SYSTEM", unique = true)
  private Long system;

  @Column(name = "UNIT_ID")
  private String unitId;

  @Column(name = "ROLE")
  private Long role;

  @Column(name = "STATUS")
  private Long status;

  @Column(name = "UPDATED_USER")
  private String updatedUser;

  @Column(name = "UPDATED_TIME")
  private Date updatedTime;

  @Column(name = "AUDIT_UNIT_ID")
  private String auditUnitId;

  @Column(name = "LOCATION_ID")
  private String locationId;

  @Column(name = "TYPE")
  private Long type;

  public CfgRoleDataDTO toDTO() {
    CfgRoleDataDTO dto = new CfgRoleDataDTO(
        id
        , username
        , system
        , unitId
        , role
        , status
        , updatedUser
        , updatedTime
        , auditUnitId
        , locationId
        , type
    );
    return dto;
  }
}
