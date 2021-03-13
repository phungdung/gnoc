package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRChildAutoDTO;
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
@Table(schema = "OPEN_PM", name = "SR_CHILD_AUTO")
public class SRChildAutoEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.SR_CHILD_AUTO_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "SR_PARENT_CODE")
  private String srParentCode;

  @Column(name = "SERVICE_ID")
  private Long serviceId;

  @Column(name = "ROLE_CODE")
  private String roleCode;

  @Column(name = "GENERATE_NO")
  private Long generateNo;

  @Column(name = "SR_CHILD_CODE")
  private String srChildCode;

  @Column(name = "CREATE_USER")
  private String createUser;

  @Column(name = "SERVICE_GROUP")
  private String serviceGroup;

  @Column(name = "SERVICE_ARRAY")
  private String serviceArray;

  @Column(name = "COUNTRY")
  private String country;

  @Column(name = "TITLE")
  private String title;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "CREATE_SR")
  private Long createSR;

  @Column(name = "END_TIME")
  private Date endTime;

  @Column(name = "SR_UNIT")
  private Long srUnit;

  @Column(name = "COMMENT_AUTO")
  private String commentAuto;
  public SRChildAutoDTO toDTO() {
    return new SRChildAutoDTO(id, srParentCode, serviceId, roleCode, generateNo, srChildCode,
        createUser, serviceGroup, serviceArray, country, title, description, createSR, endTime,
        srUnit, commentAuto);
  }
}
