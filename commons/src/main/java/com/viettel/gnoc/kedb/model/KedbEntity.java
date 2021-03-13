package com.viettel.gnoc.kedb.model;

import com.viettel.gnoc.kedb.dto.KedbDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "ONE_TM", name = "KEDB")
public class KedbEntity {

  static final long serialVersionUID = 1L;

  @Id
//  @SequenceGenerator(name = "generator", sequenceName = "KEDB_SEQ", allocationSize = 1)
//  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "KEDB_ID", unique = true, nullable = false)
  private Long kedbId;
  @Column(name = "KEDB_CODE", unique = true, nullable = false)
  private String kedbCode;
  @Column(name = "KEDB_NAME", nullable = false)
  private String kedbName;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "INFLUENCE_SCOPE")
  private String influenceScope;
  @Column(name = "CREATE_USER_ID", nullable = false)
  private Long createUserId;
  @Column(name = "CREATE_USER_NAME")
  private String createUserName;
  @Column(name = "RECEIVE_USER_ID")
  private Long receiveUserId;
  @Column(name = "PT_TT_RELATED")
  private String ptTtRelated;
  @Column(name = "TYPE_ID", nullable = false)
  private Long typeId;
  @Column(name = "SUB_CATEGORY_ID", nullable = false)
  private Long subCategoryId;
  @Column(name = "VENDOR")
  private String vendor;
  @Column(name = "SOFTWARE_VERSION")
  private String softwareVersion;
  @Column(name = "TT_WA")
  private String ttWa;
  @Column(name = "RCA")
  private String rca;
  @Column(name = "PT_WA")
  private String ptWa;
  @Column(name = "SOLUTION")
  private String solution;
  @Column(name = "WORKLOG")
  private String worklog;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME", nullable = false)
  private Date createdTime;
  @Column(name = "KEDB_STATE")
  private Long kedbState;
  @Column(name = "NOTES")
  private String notes;
  @Column(name = "RECEIVE_USER_NAME")
  private String receiveUserName;
  @Column(name = "HARDWARE_VERSION")
  private String hardwareVersion;
  @Transient
  private String typeIdStr;
  @Column(name = "COMPLETER")
  private String completer;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "COMPLETE_TIME")
  private Date completedTime;
  @Column(name = "NUM_LIKE")
  private Long numLike;
  @Column(name = "NUM_VIEW")
  private Long numView;
  @Column(name = "COMMENTS")
  private String comments;
  @Column(name = "USERS_LIKE")
  private String usersLike;
  @Column(name = "UNIT_CHECK_ID")
  private Long unitCheckId;
  @Column(name = "UNIT_CHECK_NAME")
  private String unitCheckName;
  @Column(name = "PARENT_TYPE_ID")
  private Long parentTypeId;

  public KedbDTO toDTO() {
    return new KedbDTO(kedbId, kedbCode, kedbName, description, influenceScope, createUserId,
        createUserName, receiveUserId, ptTtRelated, typeId, subCategoryId, vendor, softwareVersion,
        ttWa, rca, ptWa, solution, worklog, createdTime, kedbState, notes, receiveUserName,
        hardwareVersion, typeIdStr, completer, completedTime, numLike, numView, comments, usersLike,
        unitCheckId, unitCheckName, parentTypeId);
  }
}
