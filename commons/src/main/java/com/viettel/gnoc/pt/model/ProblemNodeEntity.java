/**
 * @(#)ProblemNodeBO.java 8/24/2015 2:36 PM, Copyright 2011 Viettel Telecom. All rights reserved
 * VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.pt.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.pt.dto.ProblemNodeDTO;
import java.io.Serializable;
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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(schema = "ONE_TM", name = "PROBLEM_NODE")
public class ProblemNodeEntity implements Serializable {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "cat_item_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "PROBLEM_NODE_ID", unique = true, nullable = false)
  private Long problemNodeId;

  @Column(name = "NODE_ID")
  private Long nodeId;

  @Column(name = "NODE_CODE")
  private String nodeCode;

  @Column(name = "PROBLEM_ID")
  private Long problemId;

  @Column(name = "VENDOR")
  private String vendor;

  @Column(name = "NODE_NAME")
  private String nodeName;

  @Column(name = "IP")
  private String ip;

  @Column(name = "NATION")
  private String nation;

  public ProblemNodeDTO toDTO() {
    ProblemNodeDTO dto = new ProblemNodeDTO(problemNodeId, nodeId, nodeCode, problemId, vendor,
        nodeName, ip, nation);
    return dto;
  }
}
