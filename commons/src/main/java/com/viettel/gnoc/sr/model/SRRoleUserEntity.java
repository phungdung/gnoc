
package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
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
@Table(schema = "OPEN_PM", name = "SR_ROLE_USER")
public class SRRoleUserEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_ROLE_USER_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ROLE_USER_ID", nullable = false)
  private Long roleUserId;

  @Column(name = "ROLE_CODE", nullable = false)
  private String roleCode;

  @Column(name = "USER_NAME", nullable = false)
  private String username;

  @Column(name = "STATUS", nullable = false)
  private String status;

  @Column(name = "CREATED_USER", nullable = false)
  private String createdUser;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME", nullable = false)
  private Date createdTime;

  @Column(name = "UPDATED_USER")
  private String updatedUser;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "UPDATED_TIME")
  private Date updatedTime;

  @Column(name = "COUNTRY", nullable = false)
  private String country;

  @Column(name = "UNIT_ID")
  private Long unitId;

  @Column(name = "IS_LEADER")
  private Long isLeader;

  public SRRoleUserInSideDTO toDTO() {
    SRRoleUserInSideDTO dto = new SRRoleUserInSideDTO(
        roleUserId
        , roleCode
        , username
        , status
        , createdUser
        , createdTime
        , updatedUser
        , updatedTime
        , country
        , unitId
        , isLeader
    );
    return dto;
  }
}

