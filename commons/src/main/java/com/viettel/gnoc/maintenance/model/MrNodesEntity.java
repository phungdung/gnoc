package com.viettel.gnoc.maintenance.model;


import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * @author Dunglv3
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_NODES")
public class MrNodesEntity {
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_NODES_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "MR_NODE_ID", unique = true, nullable = false)
  private Long mrNodeId;

  @Column(name = "SCHEDULE_TEL_ID")
  private String scheduleTelId;

  @Column(name = "MR_ID")
  private String mrId;

  @Column(name = "CR_ID")
  private String crId;

  @Column(name = "WO_ID")
  private String woId;

  @Column(name = "NODE_IP")
  private String nodeIp;

  @Column(name = "NODE_CODE")
  private String nodeCode;

  @Column(name = "NODE_NAME")
  private String nodeName;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "COMMENTS")
  private String comments;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "UPDATE_DATE")
  private Date updateDate;

  public MrNodesDTO toDTO(){
      MrNodesDTO mrNodesDTO = new MrNodesDTO(
          StringUtils.isStringNullOrEmpty(mrNodeId)? null : String.valueOf(mrNodeId),
          scheduleTelId,
          mrId,
          crId,
          woId,
          nodeIp,
          nodeCode,
          nodeName,
          status,
          comments,
          updateDate == null ? null : updateDate.toString()
      );
      return mrNodesDTO;
  }
}
