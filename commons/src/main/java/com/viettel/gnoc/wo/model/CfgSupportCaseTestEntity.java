package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.CfgSupportCaseTestDTO;
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
 * @author ITSOL
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CFG_SUPPORT_CASE_TEST")
public class CfgSupportCaseTestEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CFG_SUPPORT_CASE_TEST_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;
  @Column(name = "CFG_SUPPORT_CASE_ID")
  private Long cfgSuppportCaseId;
  @Column(name = "TEST_CASE_NAME")
  private String testCaseName;
  @Column(name = "FILE_REQUIRED")
  private Long fileRequired;


  public CfgSupportCaseTestDTO toDto() {
    return new CfgSupportCaseTestDTO(id, cfgSuppportCaseId, testCaseName, fileRequired);
  }


}
