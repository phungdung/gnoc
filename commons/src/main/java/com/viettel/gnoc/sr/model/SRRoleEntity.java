
package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRRoleDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "OPEN_PM", name = "SR_ROLE")
public class SRRoleEntity {

  static final long serialVersionUID = 1L;

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_ROLE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ROLE_ID", nullable = false)
  private Long roleId;

  @Column(name = "ROLE_CODE")
  private String roleCode;

  @Column(name = "ROLE_NAME")
  private String roleName;

  @Column(name = "CREATED_USER")
  private String createdUser;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "UPDATED_USER")
  private String updatedUser;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "UPDATED_TIME")
  private Date updatedTime;

  @Column(name = "COUNTRY")
  private String country;

  @Column(name = "GROUP_ROLE")
  private String groupRole;

  @Column(name = "PARENT_CODE")
  private String parentCode;

  public SRRoleDTO toDTO() {
    SRRoleDTO dto = new SRRoleDTO(
        roleId
        , roleCode
        , roleName
        , createdUser
        , createdTime
        , updatedUser
        , updatedTime
        , country
        , groupRole
        , parentCode
    );
    return dto;
  }
}

