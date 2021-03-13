
package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRRoleActionDTO;
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
@Table(schema = "OPEN_PM", name = "SR_ROLE_ACTIONS")
public class SRRoleActionsEntity {

  static final long serialVersionUID = 1L;

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_ROLE_ACTIONS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ROLE_ACTION_ID", nullable = false)
  private Long roleActionId;

  @Column(name = "ROLE_TYPE")
  private String roleType;

  @Column(name = "ROLE_CODE")
  private String roleCode;

  @Column(name = "CURRENT_STATUS")
  private String currentStatus;

  @Column(name = "NEXT_STATUS")
  private String nextStatus;

  @Column(name = "ACTIONS")
  private String actions;

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

  @Column(name = "SERVICE_CODE")
  private String serviceCode;

  @Column(name = "FLOW_ID")
  private Long flowId;

  @Column(name = "GROUP_ROLE")
  private String groupRole;

  public SRRoleActionDTO toDTO() {
    SRRoleActionDTO dto = new SRRoleActionDTO(
        roleActionId
        , roleType
        , roleCode
        , currentStatus
        , nextStatus
        , actions
        , createdUser
        , createdTime
        , updatedUser
        , updatedTime
        , country
        , serviceCode
        , flowId
        , groupRole
    );
    return dto;
  }
}

