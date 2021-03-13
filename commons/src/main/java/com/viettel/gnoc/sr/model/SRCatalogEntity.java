package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRCatalogDTO;
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
@Table(schema = "OPEN_PM", name = "SR_CATALOG")
public class SRCatalogEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "open_pm.SR_CATALOG_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "SERVICE_ID", unique = true, nullable = false)
  private Long serviceId;

  @Column(name = "COUNTRY")
  private String country;

  @Column(name = "SERVICE_ARRAY")
  private String serviceArray;

  @Column(name = "SERVICE_GROUP")
  private String serviceGroup;

  @Column(name = "SERVICE_CODE")
  private String serviceCode;

  @Column(name = "SERVICE_NAME")
  private String serviceName;

  @Column(name = "SERVICE_DESCRIPTION")
  private String serviceDescription;

  @Column(name = "EXECUTION_UNIT")
  private String executionUnit;

  @Column(name = "REPLY_TIME")
  private String replyTime;

  @Column(name = "EXECUTION_TIME")
  private String executionTime;

  @Column(name = "ATTACH_FILE")
  private String attachFile;

  @Column(name = "CR")
  private String cr;

  @Column(name = "WO")
  private String wo;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CREATED_USER")
  private String createdUser;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "UPDATED_USER")
  private String updatedUser;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATED_TIME")
  private Date updatedTime;

  @Column(name = "APPROVE")
  private Long approve;

  @Column(name = "IS_INPUT_CHECKING")
  private Long isInputChecking;

  @Column(name = "IS_OUTPUT_CHECKING")
  private Long isOutputChecking;

  @Column(name = "ROLE_CODE")
  private String roleCode;

  @Column(name = "FLOW_EXECUTE")
  private Long flowExecute;

  @Column(name = "AUTO_CREATE_SR")
  private String autoCreateSR;

  @Column(name = "CR_WO_CREATE_TIME")
  private Double createdTimeCRWO;

  @Column(name = "IS_ADD_DAY")
  private Long isAddDay;

  @Column(name = "RENEW_DAY")
  private Long renewDay;

  @Column(name = "AUTO_GENERATION_CYCLES")
  private Long autoGenerationCycles;

  @Column(name = "IS_CR_NODES")
  private Long isCrNodes;

  @Column(name = "MONTH_CYCLE")
  private Double monthCycle;

  @Column(name = "LAST_DATE")
  private Date lastDate;

  @Column(name = "START_AUTO_DATE")
  private Date startAutoDate;

  @Column(name = "COMMENT_AUTO")
  private String commentAuto;

  //27102020 dungpv add
  @Column(name = "NOTIFICATION")
  private String notification;

  @Column(name = "TIME_CLOSED_SR")
  private String timeClosedSR;

  public SRCatalogDTO toDTO() {
    SRCatalogDTO dto = new SRCatalogDTO(serviceId, country, serviceArray, serviceGroup, serviceCode,
        serviceName, serviceDescription, executionUnit, replyTime, executionTime,
        attachFile, cr, wo, status, createdUser, createdTime, updatedUser,
        updatedTime, approve, isInputChecking, isOutputChecking, roleCode, flowExecute,
        autoCreateSR, createdTimeCRWO != null ? createdTimeCRWO.toString() : null, isAddDay,
        renewDay, autoGenerationCycles, isCrNodes,
        monthCycle != null ? monthCycle.toString() : null, lastDate, startAutoDate, commentAuto,
        notification, timeClosedSR
    );
    return dto;
  }
}
