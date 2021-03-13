package com.viettel.gnoc.maintenance.model;


import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * @author tripm
 * @version 2.0
 * @since 12/06/2020 09:31:00
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "OPEN_PM", name = "MR_NODE_CHECKLIST")
public class MrNodeChecklistEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.MR_NODE_CHECKLIST_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "NODE_CHECKLIST_ID")
  private Long nodeChecklistId;

  @Column(name = "MR_NODE_ID")
  private Long mrNodeId;

  @Column(name = "CHECKLIST_ID")
  private Long checklistId;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "COMMENTS")
  private String comments;

  @Column(name = "CREATED_USER")
  private String createdUser;

  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "UPDATED_USER")
  private String updatedUser;

  @Column(name = "UPDATED_TIME")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date updatedTime;

  public MrNodeChecklistDTO toDTO() {
    MrNodeChecklistDTO dto = new MrNodeChecklistDTO(
        nodeChecklistId == null ? null : nodeChecklistId.toString(),
        mrNodeId == null ? null : mrNodeId.toString(),
        checklistId == null ? null : checklistId.toString(),
        status,
        createdUser,
        createdTime == null ? null : DateTimeUtils.convertDateToString(createdTime),
        comments,
        updatedUser,
        updatedTime == null ? null : DateTimeUtils.convertDateToString(updatedTime)
    );
    return dto;
  }
}
