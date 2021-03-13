
package com.viettel.gnoc.pt.model;

import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ITSOL
 * @version 2.0
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "ONE_TM", name = "PROBLEMS")
public class ProblemsEntity {

  static final long serialVersionUID = 1L;

  @Id
//  @SequenceGenerator(name = "generator", sequenceName = "PROBLEM_SEQ", allocationSize = 1)
//  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "PROBLEM_ID", unique = true, nullable = false)
  private Long problemId;
  @Column(name = "PROBLEM_CODE", unique = true, nullable = false)
  private String problemCode;
  @Column(name = "PROBLEM_NAME", nullable = false)
  private String problemName;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "TYPE_ID", nullable = false)
  private Long typeId;
  @Column(name = "SUB_CATEGORY_ID", nullable = false)
  private Long subCategoryId;
  @Column(name = "NOTES")
  private String notes;
  @Column(name = "PRIORITY_ID", nullable = false)
  private Long priorityId;
  @Column(name = "IMPACT_ID", nullable = false)
  private Long impactId;
  @Column(name = "URGENCY_ID")
  private Long urgencyId;
  @Column(name = "PROBLEM_STATE", nullable = false)
  private Long problemState;
  @Column(name = "ACCESS_ID")
  private Long accessId;
  @Column(name = "AFFECTED_NODE")
  private String affectedNode;
  @Column(name = "VENDOR")
  private String vendor;
  @Column(name = "AFFECTED_SERVICE")
  private String affectedService;
  @Column(name = "LOCATION")
  private String location;
  @Column(name = "LOCATION_ID", nullable = false)
  private Long locationId;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME", nullable = false)
  private Date createdTime;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_TIME", nullable = false)
  private Date lastUpdateTime;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "ASSIGN_TIME_TEMP")
  private Date assignTimeTemp;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "ES_RCA_TIME")
  private Date esRcaTime;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "ES_WA_TIME")
  private Date esWaTime;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "ES_SL_TIME")
  private Date esSlTime;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "STARTED_TIME")
  private Date startedTime;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "ENDED_TIME")
  private Date endedTime;
  @Column(name = "CREATE_USER_ID", nullable = false)
  private Long createUserId;
  @Column(name = "CREATE_UNIT_ID", nullable = false)
  private Long createUnitId;
  @Column(name = "CREATE_USER_NAME")
  private String createUserName;
  @Column(name = "CREATE_UNIT_NAME")
  private String createUnitName;
  @Column(name = "CREATE_USER_PHONE")
  private String createUserPhone;
  @Column(name = "DEFERRED_TIME")
  private Date deferredTime;
  @Column(name = "INSERT_SOURCE")
  private String insertSource;
  @Column(name = "IS_SEND_MESSAGE")
  private Long isSendMessage;
  @Column(name = "RELATED_TT")
  private String relatedTt;
  @Column(name = "RELATED_PT")
  private String relatedPt;
  @Column(name = "RELATED_KEDB")
  private String relatedKedb;
  @Column(name = "PT_TYPE")
  private Long ptType;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "RCA_FOUND_TIME")
  private Date rcaFoundTime;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "WA_FOUND_TIME")
  private Date waFoundTime;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "SL_FOUND_TIME")
  private Date slFoundTime;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CLOSED_TIME")
  private Date closedTime;
  @Column(name = "RCA")
  private String rca;
  @Column(name = "WA")
  private String wa;
  @Column(name = "SOLUTION")
  private String solution;
  @Column(name = "SOLUTION_TYPE")
  private Long solutionType;
  @Column(name = "RECEIVE_UNIT_ID")
  private Long receiveUnitId;
  @Column(name = "RECEIVE_USER_ID")
  private Long receiveUserId;
  @Column(name = "TIME_USED")
  private Double timeUsed;
  @Column(name = "WORKLOG")
  private String worklog;
  @Column(name = "INFLUENCE_SCOPE")
  private String influenceScope;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "DELAY_TIME")
  private Date delayTime;
  @Column(name = "RCA_TYPE")
  private Long rcaType;
  @Column(name = "CATEGORIZATION", nullable = false)
  private Long categorization;
  @Column(name = "STATE_CODE")
  private String stateCode;
  @Column(name = "PT_RELATED_TYPE")
  private Long ptRelatedType;
  @Column(name = "CONTACT_INFO")
  private String contactInfo;
  @Column(name = "PM_GROUP")
  private Long pmGroup;
  @Column(name = "CLOSE_CODE")
  private Long closeCode;
  @Column(name = "ASSIGNED_TIME")
  private Date assignedTime;
  @Column(name = "PM_ID")
  private Long pmId;
  @Column(name = "PM_USER_NAME")
  private String pmUserName;
  @Column(name = "SOFTWARE_VERSION")
  private String softwareVersion;
  @Column(name = "HARDWARE_VERSION")
  private String hardwareVersion;
  @Column(name = "PT_DUPLICATE")
  private String ptDuplicate;
  @Column(name = "REASON_OVERDUE")
  private String reasonOverdue;
  @Column(name = "is_chat")
  private Long isChat;
  @Column(name = "SKIP_STATUS")
  private String skipStatus;
  @Column(name = "DURATION_SOLUTION_TIME")
  private Date durationSolutionTime;
  @Column(name = "REASON_REJECT")
  private String reasonReject;
  @Column(name = "RADICAL_SOLUTION_TYPE")
  private Long radicalSolutionType;

  public ProblemsInsideDTO toDTO() {
    return new ProblemsInsideDTO(problemId, problemCode, problemName, description, typeId,
        subCategoryId,
        notes, priorityId, impactId, urgencyId, problemState.toString(),
        accessId, affectedNode, vendor,
        affectedService, location, locationId, createdTime, lastUpdateTime, assignTimeTemp,
        esRcaTime, esWaTime, esSlTime, startedTime, endedTime, createUserId, createUnitId,
        createUserName, createUnitName, createUserPhone, deferredTime, insertSource, isSendMessage,
        relatedTt, relatedPt, relatedKedb, ptType, rcaFoundTime, waFoundTime, slFoundTime,
        closedTime, rca, wa, solution, solutionType, receiveUnitId, receiveUserId, timeUsed,
        worklog, influenceScope, delayTime, rcaType, categorization, stateCode, ptRelatedType,
        contactInfo, (pmGroup), closeCode, assignedTime, pmId, pmUserName,
        softwareVersion,
        hardwareVersion, ptDuplicate, reasonOverdue, isChat, skipStatus, durationSolutionTime,
        reasonReject, radicalSolutionType);
  }

}
