/**
 * @(#)CrProcessTemplateBO.java 11/16/2015 5:23 PM, Copyright 2011 Viettel Telecom. All rights
 * reserved VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CrProcessTemplateDTO;
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

/**
 * @author kienpv
 * @version 1.0
 * @since 11/16/2015 5:23 PM
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "CR_PROCESS_TEMPLATE")
public class CrProcessTemplateEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "open_pm.cr_process_template_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CPTE_ID", unique = true, nullable = false)
  private Long cpteId;

  @Column(name = "CR_PROCESS_ID")
  private Long crProcessId;

  @Column(name = "TEMP_IMPORT_ID")
  private Long tempImportId;

  @Column(name = "FILE_TYPE")
  private Long fileType;

  public CrProcessTemplateDTO toDTO() {
    CrProcessTemplateDTO dto = new CrProcessTemplateDTO(cpteId, crProcessId, tempImportId, fileType
    );
    return dto;
  }
}

