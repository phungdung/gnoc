package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRCatalogChildDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "SR_CATALOG_CHILD")
public class SRCatalogChildEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "open_pm.SR_CATALOG_CHILD_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CHILD_ID", unique = true, nullable = false)
  private Long childId;

  @Column(name = "SERVICE_ID")
  private Long serviceId;

  @Column(name = "SERVICE_CODE")
  private String serviceCode;

  @Column(name = "SERVICE_ID_CHILD")
  private Long serviceIdChild;

  @Column(name = "SERVICE_CODE_CHILD")
  private String serviceCodeChild;

  @Column(name = "AUTO_CREATE_SR")
  private Long autoCreateSR;

  @Column(name = "UPDATED_USER")
  private String updatedUser;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATED_TIME")
  private Date updatedTime;

  @Column(name = "COMMENT_AUTO")
  private String commentAuto;

  @Column(name = "GENERATE_NO")
  private Long generateNo;

  public SRCatalogChildDTO toDTO() {
    SRCatalogChildDTO dto = new SRCatalogChildDTO(
        childId
        , serviceId
        , serviceCode
        , serviceIdChild
        , serviceCodeChild
        , autoCreateSR
        , updatedUser
        , updatedTime
        , commentAuto
        ,generateNo
    );
    return dto;
  }
}
