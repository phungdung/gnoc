
package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.CfgSupportCaseDTO;
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
@Table(name = "CFG_SUPPORT_CASE")
public class CfgSupportCaseEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CFG_SUPPORT_CASE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;
  @Column(name = "CASE_NAME")
  private String caseName;
  @Column(name = "SERVICE_ID")
  private Long serviceID;
  @Column(name = "INFRA_TYPE_ID")
  private Long infraTypeID;

  public CfgSupportCaseDTO toDTO() {
    return new CfgSupportCaseDTO(id, caseName, serviceID, infraTypeID);
  }


}
