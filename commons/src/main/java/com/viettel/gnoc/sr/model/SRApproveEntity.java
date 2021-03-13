package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRApproveDTO;
import java.util.Date;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "SR_APPROVE")
public class SRApproveEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_APPROVE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "APPROVE_ID")
  private Long approveId;

  @Column(name = "SR_ID")
  private Long srId;

  @Column(name = "APPROVE_LEVEL_1")
  private Long approveLevel1;

  @Column(name = "APPROVE_UNIT_LEVEL_1")
  private Long approveUnitLevel1;

  @Column(name = "APPROVE_LEVEL_2")
  private Long approveLevel2;

  @Column(name = "APPROVE_UNIT_LEVEL_2")
  private Long approveUnitLevel2;

  @Column(name = "APPROVE_USER_LEVEL_1")
  private String approveUserLevel1;

  @Column(name = "APPROVE_DATE_LEVEL_1")
  private Date approveDateLevel1;

  @Column(name = "APPROVE_USER_LEVEL_2")
  private String approveUserLevel2;

  @Column(name = "APPROVE_DATE_LEVEL_2")
  private Date approveDateLevel2;

  public SRApproveDTO toDTO() {
    return new SRApproveDTO(approveId, srId
        , approveLevel1, approveUnitLevel1
        , approveLevel2, approveUnitLevel2
        , approveUserLevel1
        , approveDateLevel1
        , approveUserLevel2
        , approveDateLevel2
        , null
        , null
        , null
        , null);
  }

}
