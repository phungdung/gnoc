/**
 * @(#)OdTypeBO.java 5/8/2015 4:40 PM, Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.od.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.od.dto.OdTypeDetailDTO;
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
import org.hibernate.annotations.Nationalized;

/**
 * @author
 * @version 2.0
 * @since 11/03/2018 4:40 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "OD_TYPE_DETAIL")
public class OdTypeDetailEntity {

  static final long serialVersionUID = 2L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OD_TYPE_DETAIL_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true)
  private Long id;

  @Column(name = "PRIORITY_ID", nullable = false)
  @Nationalized
  private Long priorityId;

  @Column(name = "PROCESS_TIME", nullable = false)
  @Nationalized
  private Double processTime;

  @Column(name = "OD_TYPE_ID")
  private Long odTypeId;

  public OdTypeDetailDTO toDTO() {
    return new OdTypeDetailDTO(id, priorityId, processTime, odTypeId);
  }
}
